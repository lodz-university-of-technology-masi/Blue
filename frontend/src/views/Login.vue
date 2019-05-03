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
  </div>
</template>

<!--TODO: how to add processing scss to project, check how to reuse styles
<style lang="scss">

  label {
    display: block;
    margin-bottom: 4px;
  }
  .login-heading {
    margin-bottom: 16px;
    text-align: center;
  }
  .form-control {
    margin-bottom: 24px;
    text-align: center;
  }
  .mb-more {
    margin-bottom: 42px;
  }
  .login-form {
    max-width: 500px;
    margin: auto;
  }
  .login-input {

    width: 30%;
    font-size: 16px;
    padding: 12px 16px;
    outline: 0;
    border-radius: 3px;
    border: 1px solid lightgrey;
  }
  .btn-submit {
    width: 30%;
    padding: 14px 16px;
    font-size: 18px;
    font-weight: bold;
    background: #60BD4F;
    color: white;
    border-radius: 3px;
    cursor: pointer;
    &:hover {
      background: darken(#60BD4F, 10%);
    }
  }
</style>
-->
<script>
  export default {
    data(){
      return {
        username : "",
        password : ""
      }
    },
    methods : {
      handleSubmit(e){
        e.preventDefault()
        if (this.password.length > 0) {
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
                this.$router.push('/')
              }
            })
            .catch(function (error) {
              console.error(error.response);
            });
        }
      }
    }
  }
</script>
