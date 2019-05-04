<template>
  <div>
    <h4>Select your language</h4>
    <b-form>
      <b-dropdown id="dropdown-role" :text="onSelectedOption" class="m-md-2">
        <b-dropdown-item
          v-for="role in availableLanguages"
          :key="role.id"
          :value="role.name"
          @click="onSelectedOption = role.name"
        >{{role.name}}</b-dropdown-item>
      </b-dropdown>
    </b-form>
    <button type="submit" @click="languageSelected">
      Select
    </button>
  </div>
</template>
<script>
    export default {
        name: "SelectTest",
      data() {
        return {
          onSelectedOption: "Choose language",
          availableLanguages: []
        };
      },
      methods: {
        getLanguages: function() {
          this.$http({
            url: 'http://localhost:8080/api/languages',
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
        languageSelected: function() {

        }
      },
      mounted() {
          this.getLanguages();
      }
    }
</script>

<style scoped>

</style>
