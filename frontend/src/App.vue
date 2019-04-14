<template>
  <div id="app">
    <div id="nav">
      <AppNavbar/>
    </div>
    <router-view/>
  </div>
</template>

<script>
import AppNavbar from "./components/AppNavbar.vue";

export default {
  name: "app",
  components: {
    AppNavbar
  }
};
</script>

<style>
#app {
  font-family: "Avenir", Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}
#nav {
  padding: 30px;
}

#nav a {
  font-weight: bold;
  color: #2c3e50;
}

ul {
  list-style-type: none;
}

#nav a.router-link-exact-active {
  color: #126ac1;
}
</style>
