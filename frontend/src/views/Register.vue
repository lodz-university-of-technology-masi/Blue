<template>
  <div>
    <h4>Register</h4>
    <form>
      <label for="username">Name</label>
      <div>
        <input id="username" type="text" v-model="username" required autofocus>
      </div>

      <label for="password">Password</label>
      <div>
        <input id="password" type="password" v-model="password" required>
      </div>

      <label for="password-confirm">Confirm Password</label>
      <div>
        <input id="password-confirm" type="password" v-model="password_confirmation" required>
      </div>

      <label for="firstName">First Name</label>
      <div>
        <input id="firstName" type="text" v-model="firstName" required autofocus>
      </div>

      <label for="lastName">Last Name</label>
      <div>
        <input id="lastName" type="text" v-model="lastName" required autofocus>
      </div>

      <div>
        <button type="submit" @click="handleSubmit()">
          Register
        </button>
      </div>
    </form>
  </div>
</template>


<script>
  export default {
    props : ["nextUrl"],
    data(){
      return {
        username : "",
        password : "",
        password_confirmation : "",
        firstName : "",
        secondName : ""
      }
    },
    methods : {
      handleSubmit(e) {
        e.preventDefault();

        if (this.password === this.password_confirmation && this.password.length > 0)
        {
          let url = "http://localhost:8088/signUp";
          this.$http.post(url, {
            username: this.username,
            password: this.password,
            firstName: this.firstName,
            lastName: this.lastName
          })
            .then(response => {
              this.$emit('registered');
              this.$router.push('/login');
            })
            .catch(function (error) {
              console.error(error.response);
            });
        } else {
          this.password = "";
          this.password_confirmation = "";

          return alert("Passwords do not match")
        }
      }
    }
  }
</script>
