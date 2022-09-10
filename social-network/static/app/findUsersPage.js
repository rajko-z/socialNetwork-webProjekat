Vue.component("find-user-page", {
    data: function () {
        return {
            name: "",
            surname: "",
            sortBy:"",
            dateOfBirthMin:"1922-01-01",
            dateOfBirthMax: "2022-12-12",
            users:[]
        }
    },

    mounted() {

    },

    methods: {
        search :function(event) {
            event.preventDefault();
            const postRequest = {
                "name":this.name,
                "surname": this.surname,
                "dateOfBirthMin": this.dateOfBirthMin,
                "dateOfBirthMax": this.dateOfBirthMax,
                "sortBy": this.sortBy
            };

            window.API.post("searchUsers",postRequest).then(res => {
                this.users = res.data;
                console.log(res.data);
                console.log(this.users);


            }).catch(err => {


            })
        }
    },


    template: `
<div >
    <h2>Find user page</h2>
    <CForm class ="row g-5 mx-auto  justify-content-center"> 
        <div class="form-group">
            <input v-model="name" type="text" class="form-control" placeholder="Name" value="" />
       </div>
       <div class="form-group">
            <input v-model="surname" type="text" class="form-control" placeholder="Surname" value="" />
        </div>
          <div class="form-group">
              <label >Older than:</label>
        </div>
        <div class="form-group">
            
            <input v-model="dateOfBirthMin" type="date" class="form-control" placeholder="Your Date of birth *" value="" />
        </div>
        <div class="form-group">
              <label >Younger than:</label>
        </div>
        <div class="form-group">
            <input v-model="dateOfBirthMax" type="date" class="form-control" placeholder="Your Date of birth *" value="" />
        </div>
         <div class="form-group">
            <input v-on:click="search($event)" type="submit" class="btnSearch" value="Search" />
        </div>
        
       
    </CForm>
      <div class="justify-content-end">
            <label><p>Sort by:</p></label><br/>
            <input v-model="sortBy" type="radio"  value="name">
            <label>Name</label>
            <input v-model="sortBy" type="radio" value="surname">
            <label>Surname</label>     
             <input v-model="sortBy" type="radio"  value="birthday">
            <label>Birthday</label>          
        </div>
        
       <div class="reg-container"> 
            <user-info v-for="user in users" :user="user" view="small"></user-info>
            <h5 v-if="!users.length">No users found.</h5>
       </div>
        
        
     
</div>
    `


});