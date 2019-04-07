<template>
  <div class="card bg-light mb-3 list-item">
    <div class="card-body">
      <b-row no-gutters>
        <b-col>
          <h5 class="card-title subordinate-text">{{subordinate.firstName}} {{subordinate.lastName}}</h5>
        </b-col>
        <b-col>
          <p class="card-text subordinate-text">{{subordinate.role}}</p>
        </b-col>
        <b-col>
          <b-button href="#" class="button-left-margin" variant="primary">Edit</b-button>
          <b-button href="#" class="button-left-margin" variant="danger">Delete</b-button>
        </b-col>
      </b-row>
    </div>
  </div>
</template>

<script>
export default {
  name: "SubordinateCard",
  props: {
    subordinate: Object
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
</style>
