<template>
  <div class="manage_positions">
    <ul id="subordinatesList">
      <li v-for="subordinate in subordinates" :key="subordinate.id">
        <SubordinateCard :subordinate="subordinate"></SubordinateCard>
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
      //   {
      //     id: 1,
      //     firstName: "Maciej",
      //     lastName: "Wyrzuc",
      //     role: "Moderator"
      //   },
      //   {
      //     id: 2,
      //     firstName: "Joanna",
      //     lastName: "Górczak",
      //     role: "Redactor"
      //   },
      //   {
      //     id: 3,
      //     firstName: "Jerzy",
      //     lastName: "Pakuła",
      //     role: "Redactor"
      //   },
      //   {
      //     id: 4,
      //     firstName: "Maciej",
      //     lastName: "Liźniewicz",
      //     role: "Candidate"
      //   }
      // ]
    };
  },
  methods: {
    getSubordinates: function () {
      this.loading = true;
      this.$http({
        url: 'http://localhost:8088/api/users',
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
    },
    getRoles: function() {
      console.log(localStorage.getItem('jwt'));
      this.$http({
        url: 'http://localhost:8088/api/roles',
        headers: {
          'Authorization': localStorage.getItem('jwt')
        }
      }).then(response => {
        if(response.status === 200) {
          console.log(response.data);
          this.availableRoles = response.data;
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
    this.getRoles();
  },
};
</script>

<style>
ul {
  vertical-align: middle;
  align-items: center;
}

</style>
