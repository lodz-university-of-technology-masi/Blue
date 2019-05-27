<template>
  <div class="home">
    <h4>Thank You!</h4>
    <br />
    <b-button size="sm" @click="redirectToList">
      Return to list
    </b-button>
  </div>
</template>

<script>
  export default {
    data() {
      return {
        items: [
          { position: -1, language: -1}
        ]
      }
    },
    methods: {
      redirectToList: function() {
        this.$http({
          url: 'http://localhost:8088/api/tests/langandpos/' +
            this.$route.params.id,
          headers: {
            'Authorization': localStorage.getItem('jwt')
          }
        }).then(response => {
          if(response.status === 200) {
            console.log(response.data);
            this.items = response.data;
            this.$router.push({ name: 'solve_tests', params: { langId: this.items.language, posId: this.items.position } })
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

        })
      },
    }
  }
</script>

