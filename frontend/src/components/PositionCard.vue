<template>
  <div>
    <div>
      <div v-if="position.active == true" class="card bg-light mb-3 list-item">
        <div class="card-body">
          <b-row no-gutters>
            <b-col>
              <h5 class="card-title position-text">{{position.name}}</h5>
            </b-col>
            <b-col>
              <p class="card-text position-text">Active</p>
            </b-col>
            <b-col>
              <b-button
                @click="changePositionActivity(position)"
                class="button-left-margin"
                variant="outline-danger"
              >Deactivate</b-button>
              <b-button
                @click="deleteModalShow = true"
                class="button-left-margin"
                variant="danger"
              >Delete</b-button>
            </b-col>
          </b-row>
        </div>
      </div>

      <div v-else class="card border-light bg-light mb-3 list-item">
        <div class="card-body">
          <b-row no-gutters>
            <b-col>
              <h5 class="card-title inactive-position-text">{{position.name}}</h5>
            </b-col>
            <b-col>
              <p class="card-text inactive-position-text">Inactive</p>
            </b-col>
            <b-col>
              <b-button
                @click="changePositionActivity(position)"
                class="button-left-margin"
                variant="outline-primary"
              >Activate</b-button>
              <b-button
                @click="deleteModalShow = true"
                class="button-left-margin"
                variant="danger"
              >Delete</b-button>
            </b-col>
          </b-row>
        </div>
      </div>
    </div>
    <div>
      <b-modal class="delete-modal" ok-variant="danger" v-model="deleteModalShow" hide-header>
        Please confirm that you want to delete position
        <b>{{position.name}}</b>
        <div slot="modal-ok" @click="deletePosition(position)">Confirm</div>
      </b-modal>
    </div>
  </div>
</template>

<script>
export default {
  name: "PositionCard",
  props: {
    position: Object
  },
  data() {
    return {
      deleteModalShow: false
    };
  },
  methods: {
    changePositionActivity: function(position) {
      position.active = !position.active;
      console.log(localStorage.getItem("jwt"));
      this.$http({
        url: "/api/positions/" + position.id,
        method: "PUT",
        headers: {
          Authorization: localStorage.getItem("jwt"),
          Accept: "application/json",
          "Content-Type": "application/json"
        },
        data: JSON.stringify(position)
      })
        .then(response => {
          if (response.status === 200) {
            console.log("Position activity changed!");
            this.$emit("refreshPositions");
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
    deletePosition: function(position) {
      console.log(localStorage.getItem("jwt"));
      this.$http({
        url: "/api/positions/" + position.id,
        method: "DELETE",
        headers: {
          Authorization: localStorage.getItem("jwt"),
          Accept: "application/json"
        }
      })
        .then(response => {
          if (response.status === 200) {
            console.log("Position deleted!");
            this.$emit("refreshPositions");
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
.position-text {
  font-weight: 550;
}

.inactive-position-text {
  font-weight: 550;
  color: rgb(131, 131, 131);
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
