<template>
  <div>
    <div class="jumbotron bg-light" v-if="testLoaded">
      <h1 class="display-custom">{{test.name}}</h1>
      <p class="display-custom-2"><b>Position: </b>{{test.position.name}}</p>
      <p class="display-custom-2"><b>Language: </b>{{test.language.name}}</p>
    </div>
    <div>
      <button
        type="button"
        @click="initNewQuestionModalValues()"
        class="margin-bottom btn btn-success btn-lg"
      >Add new question</button>
      <b-button @click="goToWiki()" class="margin-bottom button-left-margin btn btn-success btn-lg">Wiki</b-button>
      <b-button @click="goToSynonyms()" class="margin-bottom button-left-margin btn btn-success btn-lg">Synonyms</b-button>
      <ul id="questionList">
        <li v-for="question in questions" :key="question.id">
          <QuestionCard @refreshQuestions="getQuestions" :question="question"></QuestionCard>
        </li>
      </ul>

    </div>

    <div>
      <b-modal class="add-position-modal" v-model="addQuestionModalShow" title="Add new question">
        <div>
          <b-row class="all-margin">
            <b-col sm="3">
              <label>Question Type</label>
            </b-col>
            <b-col sm="9">
              <b-form-select v-model="newQuestionType" :options="types"></b-form-select>
            </b-col>
          </b-row>
            <b-row>
              <label class="all-margin">Question Content</label>
            </b-row>
            <b-row>
              <b-form-input v-model="newQuestionContent" class="all-margin"></b-form-input>
            </b-row>
          <div v-show="newQuestionType === 'W'">
            <b-row>
              <label class="all-margin" >Select amount of answers</label>
            </b-row>
            <b-row>
              <b-form-select v-model="questionCount" :options="[1,2,3,4,5]" class="all-margin"></b-form-select>
            </b-row>
            <b-row>
              <label class="all-margin" >Question answers</label>
            </b-row>
            <b-row v-for="i in questionCount">
              <b-form-input v-model="newQuestionAnswers[i]" class="all-margin"></b-form-input>
            </b-row>
          </div>
          <div v-show="newQuestionType === 'S'">
            <b-row>
              <label class="all-margin" >Select minimum</label>
            </b-row>
            <b-row>
              <b-form-input v-model="newQuestionAnswers[0]" class="all-margin"></b-form-input>
            </b-row>
            <b-row>
              <label class="all-margin" >Select maximum</label>
            </b-row>
            <b-row>
              <b-form-input v-model="newQuestionAnswers[1]" class="all-margin"></b-form-input>
            </b-row>
          </div>

        </div>
        <div @click="addQuestion()" slot="modal-ok">Save</div>
      </b-modal>
    </div>
  </div>
</template>

<script>
  import QuestionCard from "@/components/QuestionCard.vue";
  export default {
    components: {
      QuestionCard
    },
    data: function() {
      return {
        testId: "",
        testLoaded: false,
        test: "",
        addQuestionModalShow: false,
        questions: [],
        newQuestionType: "",
        newQuestionContent: "",
        newQuestionAnswers: [],
        newQuestion: {
          type: "",
          testId: "",
          content: "",
          possibleAnswers: []
        },
        types: [
          {value: 'O', text: 'Open'},
          {value: 'W', text: 'Choice'},
          {value: 'S', text: 'Scale'},
          {value: 'L', text: 'Numerical'},
        ],
        questionCount: 2,
      };
    },
    methods: {
      initNewQuestionModalValues: function() {
        this.addQuestionModalShow = true;
      },
      getQuestions: function() {
        this.$http({
          url: "/api/tests/questions/" + this.testId,
          headers: {
            Authorization: localStorage.getItem("jwt")
          }
        })
          .then(response => {
            if (response.status === 200) {
              this.questions = response.data;
            }
          })
          .catch(function(error) {})
          .then(function() {});
      },
      addQuestion: function() {
        this.newQuestion.type = this.newQuestionType;
        this.newQuestion.testId = this.testId;
        this.newQuestion.content = this.newQuestionContent;
        this.newQuestion.possibleAnswers = this.newQuestionAnswers;
        this.$http({
          url: "/api/question",
          method: "POST",
          headers: {
            Authorization: localStorage.getItem("jwt"),
            Accept: "application/json",
            "Content-Type": "application/json"
          },
          data: JSON.stringify(this.newQuestion)
        })
          .then(response => {
            if (response.status === 200) {
              console.log("New question added!");
              this.getQuestions();
            }
          })
          .catch(function(error) {})
          .then(function() {});
      },
      getTest: function() {
        this.$http({
          url: "/api/tests/" + this.$route.params.testId,
          method: "get",
          headers: {
            Authorization: localStorage.getItem("jwt"),
            Accept: "application/json",
            "Content-Type": "application/json"
          },
        })
          .then(response => {
            if (response.status === 200) {
              this.test = response.data;
              this.testLoaded = true
            }
          })
          .catch(function(error) {})
          .then(function() {});
      },
      goToWiki: function(){
        if(this.test.language.name === 'PL'){
          window.open('https://pl.wikipedia.org/wiki/' + window.getSelection().toString(), '_blank');
        }
        if(this.test.language.name === 'EN'){
          window.open('https://en.wikipedia.org/wiki/' + window.getSelection().toString(), '_blank');
        }
      },
      goToSynonyms: function(){
        if(this.test.language.name === 'PL'){
          window.open('https://www.synonimy.pl/synonim/' + window.getSelection().toString(), '_blank');
        }
        if(this.test.language.name === 'EN'){
          window.open('http://www.synonymy.com/results.php?word=' + window.getSelection().toString(), '_blank');
        }
      }
    },
    mounted: function() {
      this.testId = this.$route.params.testId;
      this.getTest();
      this.getQuestions();
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
    margin-left: 10px;
  }

  .bottom-margin {
    margin-bottom: 10px;
  }

  .all-margin {
    margin: 5px 10px 10px;
  }

  .jumbotron {
    padding: 1rem 1rem !important;
    margin-left: 25%;
    margin-right: 25%;
  }

  .display-custom {
    font-size: 2rem;
    font-weight: 400;
    line-height: 1.2;
  }

  .display-custom-2 {
    font-size: 1.3rem;
    font-weight: 300;
    line-height: 1.2;
    margin: 2%;
  }
</style>
