<template>
  <div>
    <div class="card bg-light mb-3 list-item">
      <div class="card-body">
          <b-row no-gutters>
          <b-col sm="3">
            <h5 class="card-text question-text">{{test.name}}</h5>
          </b-col>
          <b-col sm="1">
            <p class="card-title question-text">{{test.language.name}}</p>
          </b-col>
          <b-col sm="3">
            <p class="card-title question-text">{{test.position.name}}</p>
          </b-col>
          <b-col sm="5">
            <b-button @click="exportTestToFile()" class="button-left-margin" variant="secondary">Export</b-button>
            <b-button @click="initTranslateModalValues()" class="button-left-margin" variant="secondary">Translate</b-button>
            <b-button @click="goToWiki()" class="button-left-margin" variant="secondary">Wiki</b-button>
            <b-button @click="goToSynonyms()" class="button-left-margin" variant="secondary">Synonyms</b-button>
            <b-button @click="initEditModalValues()" class="button-left-margin" variant="primary">Edit</b-button>
            <b-button @click="editTestQuestions(test.id)" class="button-left-margin" variant="primary">Edit questions</b-button>
            <b-button
              @click="deleteModalShow = !deleteModalShow"
              class="button-left-margin"
              variant="danger"
            >Delete</b-button>
          </b-col>
        </b-row>
      </div>
    </div>

    <!--EDIT TEST MODAL-->
    <div>
      <b-modal class="edit-modal" v-model="editModalShow" title="Edit test">
        <div>
          <b-row class="bottom-margin">
            <b-col sm="3">
              <label>Name</label>
            </b-col>
            <b-col sm="9">
              <b-form-input
                key='TestName'
                v-model="updateTestName"
                class="bottom-margin"
                :title="'TestName'"
              ></b-form-input>
            </b-col>
          </b-row>

          <b-row class="bottom-margin">
            <b-col sm="3">
              <label>Position</label>
            </b-col>
            <b-col>
              <b-form-select v-model="updateTestPosition" :options="positionsOptions"></b-form-select>
            </b-col>
          </b-row>

          <b-row class="bottom-margin">
            <b-col sm="3">
              <label>Language</label>
            </b-col>
            <b-col sm="9">
              <b-form-select v-model="updateTestLanguage" :options="languagesOptions"></b-form-select>
            </b-col>
          </b-row>
        </div>
        <div slot="modal-ok" @click="saveEditModalValues(test.id)">Save</div>
      </b-modal>
    </div>

    <div>
      <b-modal class="edit-modal" v-model="translateModalShow" title="Translate test">
        <div>
          <b-row class="bottom-margin">
            <b-col sm="3">
              <label>Translated test name</label>
            </b-col>
            <b-col sm="9">
              <b-form-input
                key='TestName'
                v-model="testTranslatedName"
                class="bottom-margin"
                :title="'TestName'"
              ></b-form-input>
            </b-col>
          </b-row>

          <b-row class="bottom-margin">
            <b-col sm="3">
              <label>Language</label>
            </b-col>
            <b-col sm="9">
              <label> Test will be translated to {{testTranslatedLanguage.name}}  </label>
            </b-col>
          </b-row>
        </div>
        <div slot="modal-ok" @click="translateTest()">Translate</div>
      </b-modal>
    </div>

    <!--DELETE TEST MODAL-->
    <div>
      <b-modal class="delete-modal" ok-variant="danger" v-model="deleteModalShow" hide-header>
        Please confirm that you want to delete test 
        <b>{{test.name}}</b>
        <div slot="modal-ok" @click="deleteTest(test.id)">Confirm</div>
      </b-modal>
    </div>
  </div>
</template>

<script>
    export default {
      name: "TestCard",
      props: {
        test: Object,
        languagesOptions: Array,
        positionsOptions: Array
      },
      data() {
        return {
          editModalShow: false,
          deleteModalShow: false,
          translateModalShow: false,
          updateTestName: "",
          updateTestLanguage: "",
          updateTestPosition: "",
          updateTest: {
            name: "",
            positionId: "",
            languageId: ""
          },
          translateTestObject: {
            id: "",
            languageId: "",
            testName: ""
          },
          testTranslatedLanguage: "",
          testTranslatedName: ""
        };
      },
    methods: {
      initEditModalValues: function() {
        this.editModalShow = !this.editModalShow;
        this.updateTestName = this.test.name;
        this.updateTestLanguage = this.test.language;
        this.updateTestPosition = this.test.position;
      },
      initTranslateModalValues: function() {
        console.log(this.test.language)
        console.log(this.languagesOptions)

        this.testTranslatedLanguage = this.languagesOptions.find( e => e.value.name !== this.test.language.name);
        this.testTranslatedLanguage = this.testTranslatedLanguage.value;
        console.log(this.testTranslatedLanguage)
        this.translateModalShow = !this.translateModalShow;
      },
      exportTestToFile: function() {
        this.$http({
          url: "/api/tests/csv/export/" + this.test.id,
          method: "GET",
          headers: {
            Authorization: localStorage.getItem("jwt")
          },
          responseType: 'blob'
        })
          .then(response => {
            if (response.status === 200) {
              const url = window.URL.createObjectURL(new Blob([response.data]));
              const link = document.createElement('a');
              link.href = url;
              link.setAttribute('download', this.test.name + '.csv');
              document.body.appendChild(link);
              link.click();
            }
          })
          .catch(function(error) {})
          .then(function() {});
      },
      saveEditModalValues: function(testId) {
        this.updateTest.name = this.updateTestName;
        this.updateTest.positionId = this.updateTestPosition.id;
        this.updateTest.languageId = this.updateTestLanguage.id;
        this.$http({
          url: "/api/tests/" + testId,
          method: "PUT",
          headers: {
            Authorization: localStorage.getItem("jwt"),
            Accept: "application/json",
            "Content-Type": "application/json"
          },
          data: JSON.stringify(this.updateTest)
        })
          .then(response => {
            if (response.status === 200) {
              console.log("New values for test saved!");
              this.$emit("refreshTests");
            }
          })
          .catch(function(error) {})
          .then(function() {});
      },
      deleteTest: function(testId) {
        this.$http({
          url: "/api/tests/" + testId,
          method: "DELETE",
          headers: {
            Authorization: localStorage.getItem("jwt"),
            Accept: "application/json",
            "Content-Type": "application/json"
          }
        })
          .then(response => {
            if (response.status === 200) {
              this.$emit("refreshTests");
            }
          })
          .catch(function(error) {})
          .then(function() {});
      },
      editTestQuestions: function(testId) {
        this.$router.push('/manage_test/' + testId)
      },
      translateTest: function() {
        this.translateTestObject.testName = this.testTranslatedName;
        this.translateTestObject.languageId = this.testTranslatedLanguage.id;
        this.translateTestObject.id = this.test.id;

        this.$emit("showLoadingCircle");
        console.log(this.translateTestObject)
        this.$http({
          url: "/api/tests/translate",
          method: "POST",
          headers: {
            Authorization: localStorage.getItem("jwt"),
            "Content-Type": "application/json"
          },
          data: JSON.stringify(this.translateTestObject)
        })
          .then(response => {
            if (response.status === 200) {
              this.$emit("hideLoadingCircle");
              this.$emit("refreshTests");
            }
          })
          .catch(function(error) {
            this.$emit("hideLoadingCircle");
          })
          .then(function() {});
      }
    }
    };
</script>

<style scoped>

</style>
