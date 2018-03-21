'use strict';

var DirectorController = (function(){
     var selectors = {
         datepicker:'#datepicker',
         datepicker1:'#datepicker1'
    },
    bindEvents = function bindEvents(){
         console.log("Satrt bindEvents");
         $(selectors.datepicker).datepicker({
              setDate: new Date(),
              dateFormat: 'dd-mm-yy'
         });
         $( "#datepickerQuotation" ).datepicker({
             setDate: new Date(),
             dateFormat: 'dd/mm/yy'
        });
         $(selectors.datepicker1).datepicker({
              dateFormat: 'dd-mm-yy'
        });
         $(selectors.datepicker).datepicker('setDate', new Date());
    };

    var start = function(){
    console.log("Start controller js");
    bindEvents();

    };
    return{
         start:start
    }

}());
jQuery(function($){
  QuotationController.start();
});