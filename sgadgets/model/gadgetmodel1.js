//ID, width(300), height(150), gadgetURL

angular.module('gadgetmodel', [])
  .factory('gadgetModelCreator', function() {
    var id;
    var url;
    var width;
    var height;
    
    var gadgetModelCreator = function(){
      this.id = arguments[0] || 'fulano001';
      this.url = arguments[1] || 'https://raw.githubusercontent.com/morganedu/msugadgets/master/OAuth.xml';
      this.width = arguments[2] || "300px";
      this.height = arguments[3] || "150px";
    }

    var render = function(){
      var div = $("#"+this.id);
      div.load(this.url);
      alert("open URL: " + this.url);
    }

    return {
       gadgetModelCreator: gadgetModelCreator,
       render: render
    };
  }
);