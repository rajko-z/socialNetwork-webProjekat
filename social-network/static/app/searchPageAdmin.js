Vue.component("search-page-admin", {
    data: function () {
        return {
            name: "",
            surname: "",
            username:"",
            sortBy:"",
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
                "username": this.username,
                "sortBy": this.sortBy
            };

            window.API.post("adminSearch",postRequest).then(res => {
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
            <input v-model="username" type="text" class="form-control" placeholder="Username" value="" />
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
            <input v-model="sortBy" type="radio" value="username">
            <label>Username</label>  
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