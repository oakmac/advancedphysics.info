#advancedphysics.info

A project to put interactive advanced physics concepts on the web.

## Setup

Install Leiningen via 
[your preferred package manager](https://github.com/technomancy/leiningen/wiki/Packaging) 
or visit the 
[GitHub page](https://github.com/technomancy/leiningen) for other options.

Install [node.js](http://nodejs.org/), and package dependencies via:

    $ npm install

## Development

Let Leiningen watch and auto-compile clojurescript:

    $ lein cljsbuild auto

Let Grunt.js watch and auto-compile LESS files:

    $ grunt watch

Run a local development web server:

    $ grunt connect
