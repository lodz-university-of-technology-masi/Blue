<template>
  <div>
    <b-form>
      <h4>Select your language</h4>
      <b-form-select id="language-selector" v-model="selected" :options="availableLanguages" class="m-md-2"></b-form-select>

      <h4>Select your position</h4>
      <b-form-select id="position-selector" v-model="selected" :options="availablePositions" class="m-md-2"></b-form-select>

    </b-form>

    <button type="submit" @click="redirectTests">Select</button>
  </div>
</template>
<script>
export default {
  name: 'SelectTest',
  data () {
    return {
      availableLanguages: [],
      availablePositions: [],
      selectedPair: {
        language: '',
        position: ''
      }
    }
  },
  methods: {
    getLanguages: function () {
      this.$http({
        url: 'http://localhost:8088/api/languages',
        headers: {
          'Authorization': localStorage.getItem('jwt')
        }
      }).then(response => {
        if (response.status === 200) {
          console.log(response.data)
          this.availableLanguages = response.data.map(item => {
            return {
              value: item.id,
              text: item.name
            }
          })
        }
      }).catch(function (error) {
        if (error.response.status === 403) {
          // TODO: Handle non authorized error
          console.log('403 error')
        } else if (error.response.status === 500) {
          // TODO: Handle backend error
          console.log('Backend error')
        }
      }).then(function () {
        // this.loading = false;
      })
    },

    getPositions: function () {
      this.$http({
        url: 'http://localhost:8088/api/positions',
        headers: {
          'Authorization': localStorage.getItem('jwt')
        }
      }).then(response => {
        if (response.status === 200) {
          console.log(response.data)
          this.availablePositions = response.data.map(item => {
            return {
              value: item.id,
              text: item.name
            }
          })
        }
      }).catch(function (error) {
        if (error.response.status === 403) {
          // TODO: Handle non authorized error
          console.log('403 error')
        } else if (error.response.status === 500) {
          // TODO: Handle backend error
          console.log('Backend error')
        }
      }).then(function () {
        // this.loading = false;
      })
    },
    redirectTests: function () {
      this.selectedPair.language = this.inputLanguage
      this.selectedPair.position = this.inputPosition
      console.log(localStorage.getItem('jwt'))
      this.$http({
        url: '/api/solvetests',
        method: 'POST',
        headers: {
          Authorization: localStorage.getItem('jwt'),
          Accept: 'application/json',
          'Content-Type': 'application/json'
        },
        data: JSON.stringify(this.selectedPair)
      })/* .success(){
            var router = new VueRouter();
            this.router.go('/solvetest');
          }; */
    }
  },
  mounted () {
    this.getLanguages()
    this.getPositions()
  }
}
</script>

<style scoped>
form{
  width: 500px;
  margin: auto;
}
</style>
