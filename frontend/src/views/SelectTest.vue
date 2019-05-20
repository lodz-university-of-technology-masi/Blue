<template>
  <div>

    <b-form>
      <h4>Select your language</h4>
      <b-dropdown id="dropdown-role" :text="onSelectedLanguageOption" class="m-md-2">
        <b-dropdown-item
          v-for="role in availableLanguages"
          :key="role.id"
          :value="role.name"
          @click="onSelectedLanguageOption = role.name"
        >{{role.name}}</b-dropdown-item>
      </b-dropdown>
      <br />
      <h4>Select your position</h4>
      <b-dropdown id="dropdown-pos" :text="onSelectedPositionOption" class="m-md-2">
        <b-dropdown-item
          v-for="role in availablePositions"
          :key="role.id"
          :value="role.name"
          @click="onSelectedPositionOption = role.name"
        >{{role.name}}</b-dropdown-item>
      </b-dropdown>
    </b-form>
    <button type="submit" @click="select">
      Select
    </button>
  </div>
</template>
<script>
    export default {
        name: "SelectTest",
      data() {
        return {
          onSelectedLanguageOption: "Choose language",
          onSelectedPositionOption: "Choose position",
          availableLanguages: [],
          availablePositions: []
        };
      },
      methods: {
        getLanguages: function() {
          this.$http({
            url: 'http://localhost:8088/api/languages',
            headers: {
              'Authorization': localStorage.getItem('jwt')
            }
          }).then(response => {
            if(response.status === 200) {
              console.log(response.data);
              this.availableLanguages = response.data;
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
        getPositions: function() {
          this.$http({
            url: 'http://localhost:8088/api/positions',
            headers: {
              'Authorization': localStorage.getItem('jwt')
            }
          }).then(response => {
            if(response.status === 200) {
              console.log(response.data);
              this.availablePositions = response.data;
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
        select: function() {
          //TODO: Handle no lang selection
          //TODO: Handle no position selection
          this.$router.push('solve_tests');
        }
      },
    mounted () {
      this.getLanguages(),
      this.getPositions()
  }
}
</script>

<style scoped>

</style>
