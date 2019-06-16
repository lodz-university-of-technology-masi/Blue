<template>
  <div id="app"
       ref="capture"
       tabindex="-1"
       @keyup.shift.68="metricsGatherHandler"
       @keyup.shift.87="metricsCancelHandler"
       @keyup.shift.82="metricsFailHandler"
       @click.left = "metricsLeftMouseClickHandler"
       @click.middle = "metricsMiddleMouseClickHandler"
       @click.right = "metricsRightMouseClickHandler">
    <div id="nav" >
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
  },
  data() {
    return {
      currentMetricsTime: "",
      metrics: {
        startTime: "",
        isStarted: false,
        leftMouseClickedCount: 0,
        middleMouseClickedCount: 0,
        rightMouseClickedCount: 0,
        lastX: 0,
        lastY: 0,
        distance: 0,
        timeElapsed: 0,
        browser: "",
        fail: 0
      },
      metricsObject: {
        browser: "",
        screenWidth: "",
        screenHeight: "",
        mouseClicks: "",
        time: "",
        distance: "",
        isFailed: 0,
        errorCode: ""
      },
      window: {
        width: 0,
        height: 0
      }
    }
  },
  created() {
    window.addEventListener('resize', this.handleResize);
    this.handleResize();
  },
  destroyed() {
    window.removeEventListener('resize', this.handleResize)
  },
  methods: {
    metricsGatherHandler: async function () {
      const el = this.$refs.capture;
      const options = {
        type: 'dataURL'
      };
      let output = await this.$html2canvas(el, options);
      this.saveImage(output);
      if(!this.metrics.isStarted) {
        this.metrics.isStarted = true;
        this.metrics.startTime = new Date();
        this.metrics.fail = 0;
      } else {
        this.metrics.isStarted = false;
        this.metrics.timeElapsed = Math.abs((new Date().getTime() - this.metrics.startTime.getTime()));
        this.metrics.browser = this.detectBrowser();
        console.log(navigator.userAgent);
        this.saveMetrics();
      }
    },
    metricsCancelHandler: function () {
      this.metrics.isStarted = false
    },
    metricsFailHandler: function() {
      this.metrics.fail = 1;
      this.saveMetrics();
      this.metrics.fail = 0;
    },
    metricsLeftMouseClickHandler: function(event) {
      this.metrics.leftMouseClickedCount += 1;
      const xOffset = window.scrollX;
      const yOffset = window.scrollY;
      const currentX = event.clientX + xOffset;
      const currentY = event.clientY + yOffset;
      this.metrics.distance = this.calculateDistance(this.metrics.lastX, this.metrics.lastY, currentX, currentY)
      this.metrics.lastX = currentX;
      this.metrics.lastY = currentY;
    },
    metricsMiddleMouseClickHandler: function() {
      this.metrics.middleMouseClickedCount += 1
    },
    metricsRightMouseClickHandler: function() {
      this.metrics.rightMouseClickedCount += 1
    },
    handleResize: function() {
      this.window.width = window.innerWidth;
      this.window.height = window.innerHeight;
    },
    calculateDistance: function(x1, y1, x2, y2) {
      return Math.ceil(Math.sqrt(Math.pow((x1 - x2), 2) + Math.pow((y1 - y2), 2)));
    },
    saveImage: function(image) {
      this.$http({
        url: "/api/metrics/screenshot",
        method: "POST",
        headers: {
          Authorization: localStorage.getItem("jwt"),
          "Content-type": "text/plain"
        },
        data: image
      }).then(response => {
        console.log("Saved screenshot")
        }).then(function() {
        });
    },
    saveMetrics: function() {
      this.metricsObject.browser = this.metrics.browser;
      this.metricsObject.distance = this.metrics.distance;
      this.metricsObject.isFailed = this.metrics.fail;
      this.metricsObject.screenHeight = this.window.height;
      this.metricsObject.screenWidth = this.window.width;
      this.metricsObject.mouseClicks = this.metrics.leftMouseClickedCount + this.metrics.rightMouseClickedCount + this.metrics.middleMouseClickedCount;
      this.metricsObject.time = this.metrics.timeElapsed;
      console.log(this.metricsObject);
      this.$http({
        url: "/api/metrics",
        method: "POST",
        headers: {
          Authorization: localStorage.getItem("jwt"),
          "Content-Type" : 'application/json'
        },
        data: JSON.stringify(this.metricsObject)
      }).then(response => {
        console.log("Saved metrics data")
      }).then(function() {
      });
    },
    detectBrowser: function() {
      if(this.$browserDetect.isIE) {
        return 'I';
      }
      if(this.$browserDetect.isFirefox) {
        return 'F';
      }
      return 'C';
    }
  }
};
</script>

<style>
html,
body {
  margin: 0;
  padding: 0;
}

#app {
  font-family: "Avenir", Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  position: absolute;
  background: #eee;
  width: 100%;
  min-height: 100%;
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

.alert-msg {
  margin-top: 30px;
  margin-left: 25%;
  margin-right: 25%;
}

.alert-msg-without-margin {
  margin-left: 25%;
  margin-right: 25%;
}
</style>
