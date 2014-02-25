module.exports = function(grunt) {

  grunt.initConfig({
    
    // LESS conversion
    less: {
      options: {
        yuicompress: true
      },
      default:  {
        files: {
          'resources/public/css/main.min.css': 'resources/public/css/main.less'
        }
      }
    },

    // watch
    watch: {
      files: "resources/public/css/*.less",
      tasks: ["less"]
    },

    // static file server
    connect: {
      server: {
        options: {
          port: 6626,
          keepalive: true,
          base: 'resources/public'
        }
      }
    }

  });

  // load tasks from npm
  grunt.loadNpmTasks('grunt-contrib-less');
  grunt.loadNpmTasks('grunt-contrib-watch');
  grunt.loadNpmTasks('grunt-contrib-connect');

  grunt.registerTask('default', ['less']);

};
