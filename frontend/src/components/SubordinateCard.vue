<template>
  <div>
    <div class="card bg-light mb-3 list-item">
      <div class="card-body">
        <b-row no-gutters>
          <b-col>
            <h5
              class="card-title subordinate-text"
            >{{subordinate.firstName}} {{subordinate.lastName}}</h5>
          </b-col>
          <b-col>
            <p class="card-text subordinate-text">{{subordinate.role}}</p>
          </b-col>
          <b-col>
            <b-button
              @click="editModalShow = !editModalShow; onSelectedOption = subordinate.role"
              class="button-left-margin"
              variant="primary"
            >Edit</b-button>
            <b-button
              @click="deleteModalShow = !deleteModalShow"
              class="button-left-margin"
              variant="danger"
            >Delete</b-button>
          </b-col>
        </b-row>
      </div>
    </div>
    <div>
      <b-modal
        class="edit-modal"
        v-model="editModalShow"
        :title="'Change role for ' + subordinate.firstName + ' ' + subordinate.lastName"
      >
        <div>
          <b-form>
            <b-dropdown id="dropdown-role" :text="onSelectedOption" class="m-md-2">
              <b-dropdown-item
                v-for="role in availableRoles"
                :key="role.id"
                :value="role.name"
                @click="onSelectedOption = role.name"
              >{{role.name}}</b-dropdown-item>
            </b-dropdown>
          </b-form>
        </div>
        <div slot="modal-ok" @click="subordinate.role = onSelectedOption">Save</div>
      </b-modal>
    </div>

    <div>
      <b-modal class="delete-modal" ok-variant="danger" v-model="deleteModalShow" hide-header>
        Please confirm that you want to delete user <b>{{subordinate.firstName}} {{subordinate.lastName}}</b>
        <div slot="modal-ok">Confirm</div>
      </b-modal>
    </div>
  </div>
</template>

<script>
export default {
  name: "SubordinateCard",
  props: {
    subordinate: Object
  },
  data() {
    return {
      editModalShow: false,
      deleteModalShow: false,
      onSelectedOption: "Choose role",
      // TODO: get roles from API
      availableRoles: [
        {
          id: 1,
          name: "Moderator"
        },
        {
          id: 2,
          name: "Redactor"
        },
        {
          id: 3,
          name: "Candidate"
        }
      ]
    };
  }
};
</script>

<style scoped>
.subordinate-text {
  font-weight: 550;
}

.list-item {
  margin-left: 15%;
  margin-right: 15%;
}

.button-left-margin {
  margin-left: 10px;
}
</style>
