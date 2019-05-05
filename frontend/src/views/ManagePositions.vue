<template>
  <div>
    <div>
      <button type="button" class="margin-bottom btn btn-success btn-lg">Add new position</button>
      <ul id="positionsList">
        <li v-for="position in positions" :key="position.id">
          <PositionCard :position="position"></PositionCard>
        </li>
      </ul>
    </div>
  </div>
</template>

<script>
import PositionCard from "@/components/PositionCard.vue";

export default {
  components: {
    PositionCard
  },
  data: function() {
    return {
      positions: [
    {
        "id": 1,
        "name": "Java Developer",
        "active": true
    },
    {
        "id": 2,
        "name": "Senior C++ Developer",
        "active": true
    },
    {
        "id": 3,
        "name": "Junior C# Developer",
        "active": false
    },
    {
        "id": 4,
        "name": "C++ intern",
        "active": false
    },
    {
        "id": 5,
        "name": "Scrum Master",
        "active": true
    }
]
    };
  },
//   methods: {
//     getPositions: function() {
//       this.loading = true;
//       this.$http({
//         url: "/api/positions",
//         headers: {
//           Authorization: localStorage.getItem("jwt")
//         }
//       })
//         .then(response => {
//           if (response.status === 200) {
//             console.log(response.data);
//             this.positions = response.data;
//           }
//         })
//         .catch(function(error) {
//           if (error.response.status === 403) {
//             //TODO: Handle non authorized error
//             console.log("403 error");
//           } else if (error.response.status === 500) {
//             //TODO: Handle backend error
//             console.log("Backend error");
//           }
//         })
//         .then(function() {
//           // this.loading = false;
//         });
//     }
//   },
//   mounted: function() {
//     this.getPositions();
//   }
};
</script>

<style>
ul {
  vertical-align: middle;
  align-items: center;
}

.margin-bottom {
  margin-bottom: 30px;
}

.button-left-margin {
  margin-left: 10px;
}

.bottom-margin {
  margin-bottom: 10px;
}
</style>
