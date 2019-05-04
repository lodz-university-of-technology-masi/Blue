<template>
  <div class="manage_positions">
    <ul id="subordinatesList">
      <li v-for="subordinate in subordinates" :key="subordinate.id">
        <SubordinateCard @refreshSubordinates="getSubordinates" :subordinate="subordinate"></SubordinateCard>
      </li>
    </ul>
  </div>
</template>

<script>
import SubordinateCard from "@/components/SubordinateCard.vue";

export default {
  components: {
    SubordinateCard
  },
  data: function() {
    return {
      subordinates: [],
      loading: true,
      availableRoles: [],
    };
  },
  methods: {
    getSubordinates: function () {
      this.loading = true;
      this.$http({
        url: '/api/users/redactors/',
        headers: {
          'Authorization': localStorage.getItem('jwt')
        }
      }).then(response => {
        if(response.status === 200) {
          console.log(response.data);
          this.subordinates = response.data;
        }
      }).catch(function(error) {
        if(error.response.status === 403) {
          //TODO: Handle non authorized error
          console.log("403 error")
        } else if (error.response.status === 500) {
          //TODO: Handle backend error
          console.log("Backend error")
        }
      }).then(function() {
        // this.loading = false;
      })
    }
  },
  mounted:function() {
    this.getSubordinates();
  },
};
</script>

<style>
ul {
  vertical-align: middle;
  align-items: center;
}

</style>
