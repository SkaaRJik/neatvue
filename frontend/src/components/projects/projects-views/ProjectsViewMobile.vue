<template>
  <div>
    <v-btn bottom color="primary" fab fixed right>
      <v-icon>mdi-plus</v-icon>
    </v-btn>
    <template v-for="(item, index) in projects">
      <v-card :key="index" class="my-2">
        <v-card-title>{{ item.name }}</v-card-title>
        <v-card-text>
          <v-container text-xs-center>
            <v-row>
              <v-col></v-col>
              <v-col></v-col>
            </v-row>
            <v-row>
              <v-col cols="4"> {{ $t("Status") }}:</v-col>
              <v-col cols="8">
                <v-fab-transition>
                  <span v-if="item.status === 'In_Queue'">{{
                    $t(item.status, { size: item.inQueue })
                  }}</span>
                  <div
                    class="mx-auto"
                    v-else-if="item.status === 'In_Progress'"
                  >
                    <v-progress-linear
                      :value="progress[item.name]"
                      color="light-blue"
                      height="10"
                      striped
                    ></v-progress-linear>
                  </div>
                  <v-icon
                    color="success"
                    large
                    v-else-if="item.status === 'Done'"
                    >mdi-check
                  </v-icon>
                </v-fab-transition>
              </v-col>
            </v-row>
          </v-container>
        </v-card-text>
      </v-card>
    </template>
    <scroll-loader
      :loader-disable="disable"
      :loader-method="getProjectsList"
    ></scroll-loader>
  </div>
</template>

<script>
  import {alphabet} from "../../../utils/alphabet.util";
  import {getRandomValue} from "../../../utils/generate.utils";

  const statuses = ["In_Queue", "Done", "In_Progress"];

export default {
  name: "ProjectsViewMobile",
  data() {
    return {
      disable: false,
      page: 1,
      pageSize: 15,
      projects: [],
      progress: {}
    };
  },

  methods: {
    getProjectsList() {
      const newProjects = [];
      for (let i = 0; i < this.pageSize; i++) {
        const newProj = {
          name: "",
          trainingError: "",
          predictionError: "",
          status: ""
        };

        const maxSymbols = getRandomValue(0, 20);
        let title = "";
        for (let j = 0; j < maxSymbols; j++) {
          title += alphabet[getRandomValue(0, alphabet.length)];
        }

        const statusIndex = getRandomValue(0, statuses.length);
        const status = statuses[statusIndex];

        if (status === "In_Queue") {
          newProj.inQueue = getRandomValue(1, 4);
        } else if (status === "In_Progress") {
          this.progress = {
            ...this.progress,
            [title]: getRandomValue(0, 100)
          };
        } else {
          newProj.trainingError = Math.random();
          newProj.predictionError = Math.random();
        }

        newProj.name = title;
        newProj.status = status;

        newProjects.push(newProj);
      }
      this.projects = this.projects.concat(newProjects);
      if (this.projects.length >= 50) this.disable = true;
    }
  },

  watch: {
    page(value) {
      this.disable = value > 10;
    }
  }
};
</script>

<style scoped></style>
