<template>
  <div>
    <div class="manage_positions">
      <button
        type="button"
        @click="initNewRedactorModalValues()"
        class="margin-bottom btn btn-success btn-lg"
      >Add new redactor</button>

    <div>
      <b-alert class="alert-msg" v-model="showAlert" variant="danger" show dismissible>
        {{alertMessage}}
      </b-alert>
    </div>

      <ul id="subordinatesList">
        <li v-for="subordinate in subordinates" :key="subordinate.id">
          <SubordinateCard @refreshSubordinates="getSubordinates" :subordinate="subordinate"></SubordinateCard>
        </li>
      </ul>
    </div>

    <div>
      <b-modal class="add-redactor-modal" v-model="addRedactorModalShow" title="Add new redactor">
        <div>
          <b-row class="bottom-margin">
            <b-col sm="3">
              <label>First name</label>
            </b-col>
            <b-col sm="9">
              <b-form-input
                v-model="inputFirstName"
                class="bottom-margin"
                :title="'First name'"
              ></b-form-input>
            </b-col>
          </b-row>

          <b-row class="bottom-margin">
            <b-col sm="3">
              <label>Last name</label>
            </b-col>
            <b-col sm="9">
              <b-form-input
                v-model="inputLastName"
                class="bottom-margin"
                :title="'Last name'"
              ></b-form-input>
            </b-col>
          </b-row>

          <b-row class="bottom-margin">
            <b-col sm="3">
              <label>Username</label>
            </b-col>
            <b-col sm="9">
              <b-form-input
                v-model="inputUsername"
                class="bottom-margin"
                :title="'Username'"
              ></b-form-input>
            </b-col>
          </b-row>

          <b-row class="bottom-margin">
            <b-col sm="3">
              <label>Password</label>
            </b-col>
            <b-col sm="9">
              <b-form-input
                v-model="inputPassword"
                class="bottom-margin"
                :title="'Password'"
                type = 'password'
              ></b-form-input>
            </b-col>
          </b-row>
        </div>
        <div slot="modal-ok" @click="addRedactor()">Save</div>
      </b-modal>
    </div>
  </div>
</template>

<script>
import SubordinateCard from "@/components/SubordinateCard.vue";
import Axios from 'axios'

export default {
  components: {
    SubordinateCard
  },
  data: function() {
    return {
      showAlert: false,
      alertMessage: '',
      subordinates: [],
      loading: true,
      addRedactorModalShow: false,
      inputFirstName: "",
      inputLastName: "",
      inputUsername: "",
      inputPassword: "",
      newRedactor: {
        username: "",
        password: "",
        firstName: "",
        lastName: ""
      }
    };
  },
  methods: {
    initNewRedactorModalValues: function() {
      this.inputFirstName = "";
      this.inputLastName = "";
      this.inputUsername = "";
      this.inputPassword = "";
      this.addRedactorModalShow = true;
    },
    getSubordinates: function() {
      this.loading = true;
      this.$http({
        url: "/api/users/redactors/",
        headers: {
          Authorization: localStorage.getItem("jwt")
        }
      })
        .then(response => {
          if (response.status === 200) {
            this.subordinates = response.data.reverse();
          }
        })
        .catch(function(error) {
          if (error.response.status === 403) {
            console.log(this);
            //TODO: Handle non authorized error
            console.log("403 error");
          } else if (error.response.status === 500) {
            //TODO: Handle backend error
            console.log("Backend error");
          }
        })
        .then(function() {
          // this.loading = false;
        });
    },
    addRedactor: function() {
      if (this.inputUsername == null || this.inputUsername === "" || 
      this.inputPassword == null || this.inputPassword === "" || 
      this.inputFirstName == null || this.inputFirstName === "" || 
      this.inputLastName == null || this.inputLastName === "" ) {
        this.alertMessage = 'Couldn\'t add the new redactor. Please check if you filled all the data corectly.';
        this.showAlert = true;
        return false;
    }
    
      let _this = this;
      this.newRedactor.username = this.inputUsername;
      this.newRedactor.password = this.inputPassword;
      this.newRedactor.firstName = this.inputFirstName;
      this.newRedactor.lastName = this.inputLastName;
      console.log(localStorage.getItem("jwt"));
      this.$http({
        url: "/api/users/redactors/",
        method: "POST",
        headers: {
          Authorization: localStorage.getItem("jwt"),
          Accept: "application/json",
          "Content-Type": "application/json"
        },
        data: JSON.stringify(this.newRedactor)
      })
        .then(response => {
          if (response.status === 200) {
            console.log("New redactor added!");
            this.getSubordinates();
            _this.showAlert = false;
          }
        })
        .catch(function(error) {
          if (error.response.status === 403) {
            _this.alertMessage = 'Couldn\'t add the new redactor.';
            _this.showAlert = true;
            console.log("403 error");
          } else if (error.response.status === 500) {
            _this.alertMessage = 'Couldn\'t add the new redactor';
            _this.showAlert = true;
            console.log("Backend error");
          }
        })
        .then(function() {
          // this.loading = false;
        });
    }
  },
  mounted: function() {
    this.getSubordinates();
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
</style>
