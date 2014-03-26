angular.module('gadgetcontrol1', [])
  .controller('GadgetController', function() {
    this.attributes = {
      id:'',
      url: '',
      width: '',
      height: ''
    }
    
    this.gadgetConstructor = function(){
      this.attributes.id = arguments[0] || 'target';
      this.attributes.url = arguments[1] || 'example.html';
      this.attributes.width = arguments[2] || "300px";
      this.attributes.height = arguments[3] || "150px";
    };

    this.render = function(){
      var div = document.getElementById(this.attributes.id);

      if(div === null){
        console.log("Creating it.");

        div = document.createElement('div');
        div.id = this.attributes.id;
        div.style.width = this.attributes.width;
        div.style.height = this.attributes.height;

        /*   Remove-me please   */
        div.style.backgroundColor = "Yellow";
        /*   Remove-me please   */

        document.getElementsByTagName('body')[0].appendChild(div);
        
        console.log(div);
      }

      $('#'+this.attributes.id).load(this.attributes.url);
    };

    this.alerta = function alerta(){
      arguments[0] === undefined ? alert('hello world') : alert(arguments[0]);
    }

  }
);