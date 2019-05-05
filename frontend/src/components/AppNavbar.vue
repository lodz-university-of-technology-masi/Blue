<template>
  <div class="no-margin">
    <b-navbar toggleable="lg" type="dark" variant="info">
      <b-navbar-brand class="navbar-title" href="#">Recruitment App</b-navbar-brand>
      <b-navbar-toggle target="nav-collapse"></b-navbar-toggle>
      <b-collapse id="nav-collapse" is-nav>
        <ul class="navbar-nav mx-auto">
          <b-nav-item class="navbar-item active" centered>
            <router-link to="/">Home</router-link>
          </b-nav-item>
          <b-nav-item v-if="isModerator()" class="navbar-item active" centered>
            <router-link to="/redactors">Manage Redactors</router-link>
          </b-nav-item>
          <b-nav-item v-if="isModerator()" class="navbar-item active" centered>
            <router-link to="/positions">Manage Positions</router-link>
          </b-nav-item>
          <b-nav-item v-if="isRedactor()" class="navbar-item active" centered>
            <router-link to="/tests">Manage Tests</router-link>
          </b-nav-item>
        </ul>
        <ul class="navbar-nav ml-auto">
          <b-nav-item
            href="#"
            class="navbar-item active"
            right
            v-if="hasAuth()"
            @click="logout()"
          >Log out</b-nav-item>
          <template v-else>
            <b-nav-item class="navbar-item active" centered>
              <router-link to="/login">Log in</router-link>
            </b-nav-item>
            <b-nav-item class="navbar-item active" centered>
              <router-link to="/register">Register</router-link>
            </b-nav-item>
          </template>
        </ul>
      </b-collapse>
    </b-navbar>
  </div>
</template>

<script>
export default {
  data: () => {
    return {
      auth: false,
      role: ""
    };
  },
  methods: {
    logout: function() {
      localStorage.removeItem("jwt");
      localStorage.removeItem("authorities");
      this.$store.commit("updateAuthorities");
      this.$router.push({ path: "login" });
    },
    hasAuth: function() {
      this.auth = this.$store.state.token ? true : false;
      return this.auth;
    },
    isModerator: function() {
      this.role = this.$store.state.authorities;
      return this.role === "MODERATOR";
    },
    isRedactor: function() {
      this.role = this.$store.state.authorities;
      return this.role === "REDACTOR";
    },
    isUser: function() {
      this.role = this.$store.state.authorities;
      return this.role === "USER";
    }
  }
};
</script>

<style>
#nav {
  padding: 0 !important;
  margin-bottom: 30px !important;
  height: 100px;
}

#nav a.router-link-exact-active {
  color: #ffffffa0 !important;
}

#nav a {
  color: #ffffff !important;
}

.navbar-title {
  font-size: 22pt !important;
  font-weight: 600 !important;
  color: #ffffff !important;
}

.no-margin {
  margin: 0 !important;
}

.navbar-item {
  font-size: 12pt;
  font-weight: 500;
  margin-left: 20px;
  margin-right: 20px;
}
</style>
