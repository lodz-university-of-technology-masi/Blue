<template>
  <div class="no-margin">
    <b-navbar toggleable="lg" variant="light">
      <b-navbar-brand class="navbar-title" href="#">Recruitment App</b-navbar-brand>
      <b-navbar-toggle target="nav-collapse"></b-navbar-toggle>
      <b-collapse id="nav-collapse" is-nav>
        <ul class="navbar-nav mx-auto">
          <b-nav-item class="nav-item active" centered>
            <router-link to="/">Home</router-link>
          </b-nav-item>
          <b-nav-item class="nav-item active" centered>
            <router-link to="/positions">Manage Positions</router-link>
          </b-nav-item>
        </ul>
        <ul class="navbar-nav ml-auto">
          <b-nav-item href="#" class="navbar-item active" right>Log out</b-nav-item>
        </ul>
      </b-collapse>
    </b-navbar>
  </div>
</template>

<script>
export default {}
</script>

<style>
.navbar-title {
  font-size: 20pt;
  font-weight: 600;
  color: #2c3e50 !important;
}

.no-margin {
  margin: 0 !important;
}

.navbar-item {
  font-size: 12pt;
  font-weight: 500;
}
</style>
