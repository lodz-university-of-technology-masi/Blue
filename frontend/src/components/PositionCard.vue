<template>
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
            <b-button class="button-left-margin" variant="danger">Deactivate</b-button>
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
            <b-button class="button-left-margin" variant="primary">Activate</b-button>
          </b-col>
        </b-row>
      </div>
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
    return {};
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
