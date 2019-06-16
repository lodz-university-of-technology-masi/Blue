<template>
  <div>
    <div class="card bg-light mb-3 list-item">
      <div class="card-body">
        <b-row no-gutters>
          <b-col>
            <h5
              class="card-title subordinate-text"
            >{{subordinate.firstName}} {{subordinate.lastName}}</h5>
          </b-col>
          <b-col>
            <p class="card-text subordinate-text">{{subordinate.role.name}}</p>
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
        class="edit-modal"
        v-model="editModalShow"
        :title="'Edit ' + subordinate.firstName + ' ' + subordinate.lastName"
      >
        <div>
          <b-row class="bottom-margin">
            <b-col sm="3">
              <label>First name</label>
            </b-col>
            <b-col sm="9">
              <b-form-input v-model="inputFirstName" class="bottom-margin" :title="'First name'"></b-form-input>
            </b-col>
          </b-row>

          <b-row class="bottom-margin">
            <b-col sm="3">
              <label>Last name</label>
            </b-col>
            <b-col sm="9">
              <b-form-input v-model="inputLastName" class="bottom-margin" :title="'Last name'"></b-form-input>
            </b-col>
          </b-row>
        </div>
        <div slot="modal-ok" @click="saveEditModalValues()">Save</div>
      </b-modal>
    </div>

    <div>
      <b-modal class="delete-modal" ok-variant="danger" v-model="deleteModalShow" hide-header>
        Please confirm that you want to delete user
        <b>{{subordinate.firstName}} {{subordinate.lastName}}</b>
        <div slot="modal-ok" @click="deleteSubordinate(subordinate.id)">Confirm</div>
      </b-modal>
    </div>
  </div>
</template>

<script>
export default {
  name: "SubordinateCard",
  props: {
    subordinate: Object
  },
  data() {
    return {
      editModalShow: false,
      deleteModalShow: false,
      inputFirstName: "",
      inputLastName: ""
    };
  },
  methods: {
    initEditModalValues: function() {
      this.editModalShow = !this.editModalShow;
      this.inputFirstName = this.subordinate.firstName;
      this.inputLastName = this.subordinate.lastName;
    },
    saveEditModalValues: function() {
      var subordinateToUpdate = this.subordinate;
      subordinateToUpdate.firstName = this.inputFirstName;
      subordinateToUpdate.lastName = this.inputLastName;
      console.log(localStorage.getItem("jwt"));
      this.$http({
        url: "/api/users/redactors/" + subordinateToUpdate.id,
        method: "PUT",
        headers: {
          Authorization: localStorage.getItem("jwt"),
          Accept: "application/json",
          "Content-Type": "application/json"
        },
        data: JSON.stringify(subordinateToUpdate)
      })
        .then(response => {
          if (response.status === 200) {
            console.log("New values for redactor saved!");
            this.$emit("refreshSubordinates");
          }
        })
        .catch(function(error) {
          if (error.response.status === 403) {
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
    deleteSubordinate: function(subordinateId) {
      console.log(localStorage.getItem("jwt"));
      this.$http({
        url: "/api/users/redactors/" + subordinateId,
        method: "DELETE",
        headers: {
          Authorization: localStorage.getItem("jwt"),
          Accept: "application/json",
          "Content-Type": "application/json"
        }
      })
        .then(response => {
          if (response.status === 200) {
            console.log("Redactor successufully deleted.");
            this.$emit("refreshSubordinates");
          }
        })
        .catch(function(error) {
          if (error.response.status === 403) {
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
    }
  }
};
</script>

<style scoped>
.subordinate-text {
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
</style>
