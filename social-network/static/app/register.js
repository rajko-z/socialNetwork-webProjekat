Vue.component("register", {//userName | password | name | surname | dateOfBirth | gender | accountPrivate
    data: function() {
        return {
            name: null,
            surname: null,
            username: null,
            password: null,
            dateOfBirth: null,
            gender: null,
            isAccountPrivate: false,
            textEror:null

        }
    },

    methods: {

        registration: function(event) {
            event.preventDefault();
            const postRequest = {
                "username": this.username,
                "name":this.name,
                "surname": this.surname,
                "dateOfBirth": this.dateOfBirth,
                "gender": this.gender,
                "isAccountPrivate": this.isAccountPrivate,
                "password" : this.password
            };

            window.API.post("users", postRequest).then(res => {
                let user = {
                    "username": res.data.username,
                    "name": res.data.name,
                    "surname": res.data.surname,
                    "dateOfBirth": res.data.dateOfBirth,
                    "gender": res.data.gender,
                    "role": res.data.role,
                    "isAccountPrivate": res.data.isAccountPrivate
                };


                this.sendLoginEvent();

            }).catch(err => {
                this.textEror = res.messageerror();

            })
        }
    },









    template: `
<div class="reg-container">
  <div class="row">
        <div class="col-md-6 reg-form">
            <h3>Registration</h3>
            <form>
                <div class="form-group">
                    <input v-model="name" type="text" class="form-control" placeholder="Your Name *" value="" />
                </div>
                <div class="form-group">
                    <input v-model="surname" type="text" class="form-control" placeholder="Your Surname *" value="" />
                </div>
              
                 <div class="form-group">
                    <input v-model="username" type="text" class="form-control" placeholder="Your Username *" value="" />
                </div>
                <div class="form-group">
                    <input v-model="password" type="password" class="form-control" placeholder="Your Password *" value="" />
                </div>
               <div class="form-group">
                    <input v-model="dateOfBirth" type="date" class="form-control" placeholder="Your Date of birth *" value="" />
                </div>
                <div class="form-group">
                    <label><p>Gender:</p></label><br/>
                    <input v-model="gender" type="radio" id="Male" name="gender" value=MALE>
                    <label>Male</label>
                    <input v-model="gender" type="radio" id="Female" name="gender" value=FEMALE>
                    <label>Male</label>             
                </div>
                <div>
                    <input v-model= "isAccountPrivate" type="checkbox"  name="isPrivate" value=true>
                    <label for="vehicle1"> Private account</label><br>
                </div>
                
                <div class="form-group" v-if="errorText">
                    <label class="reg-errorMessage" >textEror</label>
                </div>
                
                <div class="form-group">
                    <input v-on:click="registration($event)" type="submit" class="registration-btnSubmit" value="Registration" />
                </div>
                
                
                
                
                
             
                
             
            </form>
        </div>
    </div>
</div>    
    `

});