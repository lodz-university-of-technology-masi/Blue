<template>
  <div class="manage_positions">
    <ul id="subordinatesList">
      <li v-for="subordinate in subordinates" :key="subordinate.id">
        <SubordinateCard :subordinate="subordinate"></SubordinateCard>
      </li>
    </ul>
  </div>
</template>

<script>
import SubordinateCard from "@/components/SubordinateCard.vue";

export default {
  components: {
    SubordinateCard
  },
  data: function() {
    return {
      subordinates: [
        {
          id: 1,
          firstName: "Maciej",
          lastName: "Wyrzuc",
          role: "Moderator"
        },
        {
          id: 2,
          firstName: "Joanna",
          lastName: "Górczak",
          role: "Redactor"
        },
        {
          id: 3,
          firstName: "Jerzy",
          lastName: "Pakuła",
          role: "Redactor"
        },
        {
          id: 4,
          firstName: "Maciej",
          lastName: "Liźniewicz",
          role: "Candidate"
        }
      ]
    };
  }
};
</script>

<style>
ul {
  vertical-align: middle;
  align-items: center;
}

</style>
