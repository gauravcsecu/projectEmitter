# projectEmitter

This Project we can use for streaming data like live cricket score, live qunatity of any commercial stuffs.
1. We provide two api
        conumser api: for consuming data 
        producer api: for producing data

2. In Memory map 
         to store different different item value we use in memory map
         and when we update value by producer api it will store here.
         
3. Emiiter 
          We used emitter concept of spring boot where multipler consumer can consume data
          and multiple producer can produce data.
          
4. Synchronise
          The latest data updated by producer store in the map and as map update we make sure this 
          value reflect to user
       
5. How to use 
          Run this project on vs code  intellije or other ide 
          Then fire the api
          
              api-1:  
              localhost:8080/producer/tommato/22 
           
     {Now they can see message updated value successfly of tommato}    
             
             api-2:  
             localhost:8080/consumer/tommato 
           
     {Now they can see updated value of tommato} this api run continuously}
          
          
           
