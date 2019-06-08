<template>
  <div>
    <div>
      <button
        type="button"
        @click="initNewTestModalValues"
        class="margin-bottom button-left-margin btn btn-success btn-lg"
      >Add new test</button>

      <button
        type="button"
        @click="initImportTestModalValues"
        class="margin-bottom button-left-margin btn btn-success btn-lg"
      >Import test from .csv file</button>
    </div>

    <div>
      <ul id="questionList">
        <li v-for="test in tests" :key="test.id">
          <TestCard @refreshTests="getTests" :test="test"
                    :languagesOptions="languagesOptions"
                    :positionsOptions="positionsOptions"></TestCard>
        </li>
      </ul>
    </div>


    <div>
      <b-modal class="add-test-modal" v-model="addTestModalShow" title="Add new test">
        <div>
          <b-row class="bottom-margin">
            <b-col sm="3">
              <label>Name</label>
            </b-col>
            <b-col sm="9">
              <b-form-input
                key='TestName'
                v-model="newTestName"
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
              <b-form-select v-model="newTestPosition" :options="positionsOptions"></b-form-select>
            </b-col>
          </b-row>


          <b-row class="bottom-margin">
            <b-col sm="3">
              <label>Language</label>
            </b-col>
            <b-col sm="9">
              <b-form-select v-model="newTestLanguage" :options="languagesOptions"></b-form-select>
            </b-col>
          </b-row>
        </div>
        <div slot="modal-ok" @click="addTest">Save</div>
      </b-modal>
    </div>

    <div>
      <b-modal class="import-test-modal" v-model="importTestModalShow" title="Import new test from .csv file">
        <div>

          <b-row class="bottom-margin">
            <b-col sm="3">
              <label>Test name</label>
            </b-col>
            <b-col sm="9">
              <b-form-input
                key="TestName"
                v-model="newTestName"
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
              <b-form-select v-model="newTestPosition" :options="positionsOptions"></b-form-select>
            </b-col>
          </b-row>

          <b-row class="bottom-margin">
            <b-col sm="3">
              <label>File</label>
            </b-col>
            <b-col sm="9">
              <input type="file" id="file" ref="file"/>
            </b-col>
          </b-row>
        </div>
        <div slot="modal-ok" @click="uploadTestFromCsv">Save</div>
      </b-modal>
    </div>
  </div>
</template>

<script>

  import TestCard from "@/components/TestCard.vue";

  export default {
    components: {
      TestCard
    },
    data: function() {
      return {
        addTestModalShow: false,
      importTestModalShow: false,
      file: '',
        tests: [],
        inputName: "",
        newTest: {
          name: "",
          positionId: null,
          languageId: null
        },
        languages: [],
        positions: [],
        languagesOptions: [],
        positionsOptions: [],
        newTestName: "",
        newTestPosition: "",
        newTestLanguage: "",
      }
    },
    methods: {
      initNewTestModalValues: function() {
        this.newTestName = "";
        this.addTestModalShow = true;
      },
    initImportTestModalValues: function() {
      this.newTestName = "";
      this.importTestModalShow = true;
    },
    uploadTestFromCsv(){
    this.file = this.$refs.file.files[0];
    let formData = new FormData();
    formData.append('file', this.file);
    formData.append('positionId', this.newTestPosition.id);
    formData.append('testName', this.newTestName);

    this.$http({
        url: "api/tests/csv/import",
        method: "POST",
        headers: {
          Authorization: localStorage.getItem("jwt")
        },
        data: formData
      })
        .then(response => {
          if (response.status === 200) {
            this.getTests();
            this.files = '';
          }
        })
        .catch(function(error) {})
        .then(function() {});
  },
      getTests: function() {
        this.$http({
          url: "/api/tests",
          headers: {
            Authorization: localStorage.getItem("jwt")
          }
        })
          .then(response => {
            if (response.status === 200) {
              this.tests = response.data.reverse();
            }
          })
          .catch(function(error) {})
          .then(function() {});
      },
      getPositions: function() {
        this.$http({
          url: "/api/positions",
          headers: {
            Authorization: localStorage.getItem("jwt")
          }
        })
          .then(response => {
            if (response.status === 200) {
              this.positions = response.data.reverse();
              this.positionsOptions = response.data.map(position => ({
                'value': position, 'text': position.name
              }));
            }
          })
          .catch(function(error) {})
          .then(function() {});
      },
      getLanguages: function() {
        this.$http({
          url: "/api/languages",
          headers: {
            Authorization: localStorage.getItem("jwt")
          }
        })
          .then(response => {
            if (response.status === 200) {
              this.languages = response.data.reverse();
              this.languagesOptions = response.data.map(language => ({
                'value': language, 'text': language.name
              }));
            }
          })
          .catch(function(error) {})
          .then(function() {});
      },
      addTest: function() {
        this.newTest.name = this.newTestName;
        this.newTest.positionId = this.newTestPosition.id;
        this.newTest.languageId = this.newTestLanguage.id;
        if(this.newTest.name == null || this.newTestName === "" || this.newTest.positionId == null
          || this.newTest.languageId == null) {
          //TODO: Pop some modal that values were wrong?
          console.log("error");
          return;
        }
        this.$http({
          url: "/api/tests",
          method: "POST",
          headers: {
            Authorization: localStorage.getItem("jwt"),
            Accept: "application/json",
            "Content-Type": "application/json"
          },
          data: JSON.stringify(this.newTest)
        })
          .then(response => {
            if (response.status === 200) {
              this.getTests();
            }
          })
          .catch(function(error) {})
          .then(function() {});
      },
      deleteTest: function(test) {
        console.log(localStorage.getItem("jwt"));
        this.$http({
          url: "/api/tests/" + test.id,
          method: "DELETE",
          headers: {
            Authorization: localStorage.getItem("jwt"),
            Accept: "application/json"
          }
        })
          .then(response => {
            if (response.status === 200) {
              this.$emit("refreshTests");
            }
          })
          .catch(function(error) {})
          .then(function() {});
      }
    },
    mounted: function() {
      this.getTests();
      this.getPositions();
      this.getLanguages();
    }
  };
</script>

<style>
  ul {
    vertical-align: middle;
    align-items: center;
  }

  .margin-bottom {
    margin-bottom: 30px;
  }

.button-left-margin {
  margin-left: 30px;
}

  .bottom-margin {
    margin-bottom: 10px;
  }
</style>
