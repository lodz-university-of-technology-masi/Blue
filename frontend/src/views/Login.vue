<template>
  <div>
    <h4>Login</h4>
    <form>
      <label for="username" >Username</label>
      <div>
        <input id="username" type="username" v-model="username" required autofocus>
      </div>
      <div>
        <label for="password" >Password</label>
        <div>
          <input id="password" type="password" v-model="password" required>
        </div>
      </div>
      <div>
        <button type="submit" @click="handleSubmit">
          Login
        </button>
      </div>
    </form>

    <div>
      <b-alert
        class="alert-msg"
        v-model="showAlert"
        variant="danger"
        show
        dismissible
      >{{alertMessage}}</b-alert>
    </div>
    
  </div>
</template>

<script>
  export default {
    data(){
      return {
        username : "",
        password : "",
        showAlert: false,
        alertMessage: ""
      }
    },
    methods : {
      handleSubmit(e){
        e.preventDefault()
        if (this.password.length > 0) {
          let _this = this;
          this.$http.post('http://localhost:8088/login', {
            username: this.username,
            password: this.password
          }, {headers: {
              "Access-Control-Expose-Headers": "Authorities, Authorization"
            }})
            .then(response => {
              localStorage.setItem('jwt', response.data.Authorization);
              localStorage.setItem('authorities', response.data.Authorities);
              if(localStorage.getItem('jwt') != null) {
                this.$emit('loggedIn');
                this.$root.updateAuthorities();
                if(response.data.Authorities === 'REDACTOR') {
                  this.$router.push('/tests')
                } else if(response.data.Authorities === 'MODERATOR') {
                  this.$router.push('/redactors')
                } else if (response.data.Authorities === 'USER') {
                  this.$router.push('/select_test')
                } else {
                  this.$router.push('/')
                }
              }
            })
            .catch(function (error) {
              console.error(error.response);
              _this.alertMessage =
              "Incorrect login and/or password. Please try again.";
              _this.showAlert = true;
              return false;
            });
        }
      }
    }
  }
</script>
