<template>
  <div>
    <div class="card bg-light mb-3 list-item">
      <div class="card-body">
        <b-row no-gutters>
          <b-col>
            <h5
              class="card-text question-text"
            > {{getFullType()}}</h5>
          </b-col>
          <b-col>
            <p class="card-title question-text">{{question.content}}</p>
          </b-col>
          <b-col>
            <b-button @click="initEditModalValues()" class="button-left-margin" variant="primary">Edit</b-button>
            <b-button
              @click="deleteModalShow = !deleteModalShow"
              class="button-left-margin"
              variant="danger"
            >Delete</b-button>
          </b-col>
        </b-row>
      </div>
    </div>
    <div>
      <b-modal
        v-model="addModalShow" title="Add new question">
        <div>
          <b-row class="all-margin">
            <b-col sm="3">
              <label>Question Type</label>
            </b-col>
            <b-col sm="9">
              <b-form-select v-model="questionType" :options="types"></b-form-select>
            </b-col>
          </b-row>
          <b-row>
            <label class="all-margin">Question Content</label>
          </b-row>
          <b-row>
            <b-form-input v-model="questionContent" class="all-margin"></b-form-input>
          </b-row>
          <div class="all-margin" v-show="questionType === 'W'">
            <b-row>
              <label class="all-margin">Select amount of answers</label>
            </b-row>
            <b-row>
              <b-form-select v-model="questionCount" :options="[1,2,3,4,5]" class="all-margin"></b-form-select>
            </b-row>
            <b-row>
              <label class="all-margin">Question answers</label>
            </b-row>
            <b-row v-for="(n, i) in questionCount">
              <b-form-input v-model="questionAnswers[i]" class="all-margin"></b-form-input>
            </b-row>
          </div>
          <div v-show="questionType === 'S'">
            <b-row>
              <label class="all-margin" >Select minimum</label>
            </b-row>
            <b-row>
              <b-form-input v-model="questionAnswers[0]" class="all-margin"></b-form-input>
            </b-row>
            <b-row>
              <label class="all-margin" >Select maximum</label>
            </b-row>
            <b-row>
              <b-form-input v-model="questionAnswers[1]" class="all-margin"></b-form-input>
            </b-row>
          </div>
        </div>
        <div slot="modal-ok" @click="saveEditModalValues()">Save</div>
      </b-modal>
    </div>


    <div>
      <b-modal class="delete-modal" ok-variant="danger" v-model="deleteModalShow" hide-header>
        Please confirm that you want to delete question 
        <b>{{question.content}}</b>
        <div slot="modal-ok" @click="deleteQuestion(question.id)">Confirm</div>
      </b-modal>
    </div>
  </div>
</template>

<script>
  export default {
    name: "QuestionCard",
    props: {
      question: Object
    },
    data: function() {
      return {
        addModalShow: false,
        deleteModalShow: false,
        questionContent: "",
        questionAnswers: [],
        questionCount: "",
        questionType: "",
        types: [
          {value: 'O', text: 'Open'},
          {value: 'W', text: 'Choice'},
          {value: 'S', text: 'Scale'},
          {value: 'L', text: 'Numerical'},
        ]
      };
    },
    methods: {
      initEditModalValues: function() {
        this.addModalShow = !this.addModalShow;
        this.questionContent = this.question.content;
        this.questionCount = this.question.possibleAnswers.length;
        this.questionAnswers = this.question.possibleAnswers;
        this.questionType = this.question.type;
      },
      saveEditModalValues: function() {
        let questionToUpdate = this.question;
        questionToUpdate.type = this.questionType;
        questionToUpdate.content = this.questionContent;
        questionToUpdate.possibleAnswers = this.questionAnswers.slice(0, this.questionCount);

        this.$http({
          url: "/api/question/" + questionToUpdate.id,
          method: "PUT",
          headers: {
            Authorization: localStorage.getItem("jwt"),
            Accept: "application/json",
            "Content-Type": "application/json"
          },
          data: JSON.stringify(questionToUpdate)
        })
          .then(response => {
            if (response.status === 200) {
              console.log("New values for question saved!");
              this.$emit("refreshSubordinates");
            }
          })
          .catch(function(error) {})
          .then(function() {});
      },
      deleteQuestion: function(questionId) {
        this.$http({
          url: "/api/question/" + questionId,
          method: "DELETE",
          headers: {
            Authorization: localStorage.getItem("jwt"),
            Accept: "application/json",
            "Content-Type": "application/json"
          }
        })
          .then(response => {
            if (response.status === 200) {
              this.$emit("refreshQuestions");
            }
          }).catch(function(error) {}).then(function() {});
      },
      getFullType: function() {
        return this.types.find(e => e.value === this.question.type).text
      }
    }
  };
</script>

<style scoped>
  .question-text {
    font-weight: 550;
  }

  .list-item {
    margin-left: 15%;
    margin-right: 15%;
  }

  .button-left-margin {
    margin-left: 10px;
  }

  .bottom-margin {
    margin-bottom: 10px;
  }

  .all-margin {
    margin: 5px 10px 10px;
  }
</style>
