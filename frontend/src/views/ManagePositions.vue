<template>
  <div>
    <div>
      <button
        type="button"
        @click="initNewPositionModalValues()"
        class="margin-bottom btn btn-success btn-lg"
      >Add new position</button>

    <div>
      <b-alert class="alert-msg" v-model="showAlert" variant="danger" show dismissible>
        {{alertMessage}}
      </b-alert>
    </div>

      <ul id="positionsList">
        <li v-for="position in positions" :key="position.id">
          <PositionCard @refreshPositions="getPositions" :position="position"></PositionCard>
        </li>
      </ul>
    </div>

    <div>
      <b-modal class="add-position-modal" v-model="addPositionModalShow" title="Add new position">
        <div>
          <b-row class="bottom-margin">
            <b-col sm="3">
              <label>Name</label>
            </b-col>
            <b-col sm="9">
              <b-form-input v-model="inputName" class="bottom-margin" :title="'Name'"></b-form-input>
            </b-col>
          </b-row>
          <b-row class="bottom-margin">
            <b-col sm="3">
              <label>Activate</label>
            </b-col>
            <b-col sm="2">
              <b-form-checkbox v-model="newActive" size="30px" name="check-button" switch>
                <b v-if="newActive == true">Active</b>
                <b v-else>Inactive</b>
              </b-form-checkbox>
            </b-col>
          </b-row>
        </div>
        <div @click="addPosition()" slot="modal-ok">Save</div>
      </b-modal>
    </div>
  </div>
</template>

<script>
import PositionCard from "@/components/PositionCard.vue";
import { constants } from "fs";

export default {
  components: {
    PositionCard
  },
  data: function() {
    return {
      showAlert: false,
      alertMessage: '',
      addPositionModalShow: false,
      positions: [],
      inputName: "",
      newActive: true,
      newPosition: {
        name: "",
        active: ""
      }
    };
  },
  methods: {
    initNewPositionModalValues: function() {
      this.inputName = "";
      this.addPositionModalShow = true;
    },
    getPositions: function() {
      this.loading = true;
      this.$http({
        url: "/api/positions",
        headers: {
          Authorization: localStorage.getItem("jwt")
        }
      })
        .then(response => {
          if (response.status === 200) {
            this.positions = response.data.reverse();
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
    addPosition: function() {

      if (this.inputName == null || this.inputName === ""){
        this.alertMessage = 'Couldn\'t add the new position. Please check if you filled all the data corectly.';
        this.showAlert = true;
        return false;
    }
      this.newPosition.name = this.inputName;
      this.newPosition.active = this.newActive;
      let _this = this;
      this.$http({
        url: "/api/positions",
        method: "POST",
        headers: {
          Authorization: localStorage.getItem("jwt"),
          Accept: "application/json",
          "Content-Type": "application/json"
        },
        data: JSON.stringify(this.newPosition)
      })
        .then(response => {
          if (response.status === 200) {
            console.log("New position added!");
            this.getPositions();
          }
        })
        .catch(function(error) {
          if (error.response.status === 403) {
            //TODO: Handle non authorized error
            console.log("403 error");
          } else if (error.response.status === 500) {
            //TODO: Handle backend error
            _this.alertMessage = 'Couldn\'t add the new position - backend error';
            _this.showAlert = true;
            console.log("Backend error");
          } else {
            _this.alertMessage = 'Couldn\'t add the new redactor';
            _this.showAlert = true;
          }
        })
        .then(function() {
          // this.loading = false;
        });
    }
  },
  mounted: function() {
    this.getPositions();
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
