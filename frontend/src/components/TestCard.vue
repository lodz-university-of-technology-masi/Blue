<template>
  <div>
    <div class="card bg-light mb-3 list-item">
      <div class="card-body">
        <b-row no-gutters>
          <b-col>
            <h5
              class="card-text question-text"
            > {{test.name}}</h5>
          </b-col>
          <b-col>
            <p class="card-title question-text">{{test.language.name}}</p>
          </b-col>
          <b-col>
          <p class="card-title question-text">{{test.position.name}}</p>
        </b-col>
          <b-col>
            <b-button @click="initEditModalValues" class="button-left-margin" variant="primary">Edit</b-button>
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
          updateTestName: "",
          updateTestLanguage: "",
          updateTestPosition: "",
          updateTest: {
            name: "",
            positionId: "",
            languageId: ""
          }
        };
      },
    methods: {
      initEditModalValues: function() {
        this.editModalShow = !this.editModalShow;
        this.updateTestName = this.test.name;
        this.updateTestLanguage = this.test.language;
        this.updateTestPosition = this.test.position;
      },
      saveEditModalValues: function(testId) {
        this.updateTest.name = this.updateTestName;
        this.updateTest.positionId = this.updateTestPosition.id;
        this.updateTest.languageId = this.updateTestLanguage.id;
        console.log(this.updateTest);
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
      }
    }
    };
</script>

<style scoped>

</style>
