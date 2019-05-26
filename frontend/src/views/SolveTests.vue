<template>
  <b-container fluid>
    <!-- User Interface controls -->
    <b-row>
      <b-col md="6" class="my-1">
        <b-form-group label-cols-sm="3" label="Filter" class="mb-0">
          <b-input-group>
            <b-form-input v-model="filter" placeholder="Type to Search"></b-form-input>
            <b-input-group-append>
              <b-button :disabled="!filter" @click="filter = ''">Clear</b-button>
            </b-input-group-append>
          </b-input-group>
        </b-form-group>
      </b-col>

      <b-col md="6" class="my-1">
        <b-form-group label-cols-sm="3" label="Sort" class="mb-0">
          <b-input-group>
            <b-form-select v-model="sortBy" :options="sortOptions">
              <option slot="first" :value="null">-- none --</option>
            </b-form-select>
            <b-form-select v-model="sortDesc" :disabled="!sortBy" slot="append">
              <option :value="false">Asc</option> <option :value="true">Desc</option>
            </b-form-select>
          </b-input-group>
        </b-form-group>
      </b-col>

      <b-col md="6" class="my-1">
        <b-form-group label-cols-sm="3" label="Sort direction" class="mb-0">
          <b-input-group>
            <b-form-select v-model="sortDirection" slot="append">
              <option value="asc">Asc</option> <option value="desc">Desc</option>
              <option value="last">Last</option>
            </b-form-select>
          </b-input-group>
        </b-form-group>
      </b-col>

      <b-col md="6" class="my-1">
        <b-form-group label-cols-sm="3" label="Per page" class="mb-0">
          <b-form-select v-model="perPage" :options="pageOptions"></b-form-select>
        </b-form-group>
      </b-col>
    </b-row>

    <b-table
      show-empty
      stacked="md"
      :items="items"
      :fields="fields"
      :current-page="currentPage"
      :per-page="perPage"
      :filter="filter"
      :sort-by.sync="sortBy"
      :sort-desc.sync="sortDesc"
      :sort-direction="sortDirection"
      @filtered="onFiltered"
    >
      <template slot="name" slot-scope="row">
        {{ row.value}}
      </template>

      <!--<template slot="author" slot-scope="row">
        {{ row.value.first }} {{ row.value.last }}
      </template>-->

      <template slot="jobtitle" slot-scope="row">
        {{ row.value}}
      </template>

      <!--<template slot="actions" slot-scope="row">
        <b-button size="sm" @click="row.toggleDetails" class="mr-1">
          {{ row.detailsShowing ? 'Hide' : 'Show' }} Details
        </b-button>
        <b-button size="sm" class="mr-1">
          Edit
        </b-button>
        <b-button size="sm" class="mr-1">
          Delete
        </b-button>
      </template>-->

      <template slot="actions" slot-scope="row">
        <b-button size="sm" @click="row.toggleDetails" class="mr-1">
          Solve
        </b-button>
      </template>

      <template slot="row-details" slot-scope="row">
        <b-card>
          <ul>
            <li v-for="(value, key) in row.item" :key="key">{{ key }}: {{ value }}</li>
          </ul>
        </b-card>
      </template>
    </b-table>

    <b-row>
      <b-col md="6" class="my-1">
        <b-pagination
          v-model="currentPage"
          :total-rows="totalRows"
          :per-page="perPage"
          class="my-0"
        ></b-pagination>
      </b-col>
    </b-row>

    <b-modal :id="infoModal.id" :title="infoModal.title" ok-only @hide="resetInfoModal">
      <pre>{{ infoModal.content }}</pre>
    </b-modal>
  </b-container>
</template>

<script>
  export default {
    data() {
      return {
        items: [
          { author: { first: 'Maciej', last: 'Liźniewicz' }, name: 'Test 1', job_title: 'Junior Java Developer', creation_date: '28-04-2019', date_modified: '30-04-2019'},
          { author: { first: 'Maciej', last: 'Wyrzuc' }, name: 'Test 2', job_title: 'Web Designer', creation_date: '28-04-2019', date_modified: '30-04-2019'},
          { author: { first: 'Joanna', last: 'Górczak' }, name: 'Test 3', job_title: 'Senior Test Engineer', creation_date: '25-04-2019', date_modified: '30-04-2019'},
          { author: { first: 'Joanna', last: 'Górczak' }, name: 'Test 4', job_title: 'Junior Test Engineer', creation_date: '21-04-2019', date_modified: '30-04-2019'},
          { author: { first: 'Jerzy', last: 'Pakuła' }, name: 'Test 5', job_title: 'Senior Java Developer', creation_date: '18-04-2019', date_modified: '30-04-2019'},
          { author: { first: 'Maciej', last: 'Liźniewicz' }, name: 'Test 6', job_title: 'Junior Test Engineer', creation_date: '10-04-2019', date_modified: '30-04-2019'},
          { author: { first: 'Jerzy', last: 'Pakuła' }, name: 'Test 7', job_title: 'PHP Developer', creation_date: '27-04-2019', date_modified: '30-04-2019'},
          { author: { first: 'Maciej', last: 'Wyrzuc' }, name: 'Test 8', job_title: 'Web Designer', creation_date: '29-04-2019', date_modified: '30-04-2019'},
        ],
        fields: [
          { key: 'name', label: 'Test name', sortable: true, sortDirection: 'desc' },
          { key: 'job_title', label: 'Job title', sortable: true, class: 'text-center' },
          { key: 'actions', label: 'Actions' }
        ],
        totalRows: 1,
        currentPage: 1,
        perPage: 5,
        pageOptions: [5, 10, 15],
        sortBy: null,
        sortDesc: false,
        sortDirection: 'asc',
        filter: null,
        infoModal: {
          id: 'info-modal',
          title: '',
          content: ''
        }
      }
    },
    computed: {
      sortOptions() {
        return this.fields
          .filter(f => f.sortable)
          .map(f => {
            return { text: f.label, value: f.key }
          })
      }
    },
    mounted() {
      this.getTests()
      this.totalRows = this.items.length
    },
    methods: {
      getTests: function() {
        this.$http({
          url: 'http://localhost:8088/api/tests/solvelist/' +
            this.$route.params.langId + '/' + this.$route.params.posId,
          headers: {
            'Authorization': localStorage.getItem('jwt')
          }
        }).then(response => {
          if(response.status === 200) {
            console.log(response.data);
            this.items = response.data.map( item => {
              return{
                author: {first: item.author.firstName,
                  last: item.author.lastName},
                name: item.name,
                job_title: item.position.name,
                creation_date: item.creationDate,
                date_modified: item.modificationDate
              }
            });
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
      info(item, index, button) {
        this.infoModal.title = `Row index: ${index}`
        this.infoModal.content = JSON.stringify(item, null, 2)
        this.$root.$emit('bv::show::modal', this.infoModal.id, button)
      },
      resetInfoModal() {
        this.infoModal.title = ''
        this.infoModal.content = ''
      },
      onFiltered(filteredItems) {
        this.totalRows = filteredItems.length
        this.currentPage = 1
      }
    }
  }
</script>
