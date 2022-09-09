Vue.component("edit-basic-info", {
    data: function() {
        return {
            loggedUser: window.getCurrentUser(),

            username:  window.getCurrentUser().username,
            name:  window.getCurrentUser().name,
            surname:  window.getCurrentUser().surname,
            dateOfBirth:  window.getCurrentUser().dateOfBirth,
            gender:  window.getCurrentUser().gender,
            isAccountPrivate:  window.getCurrentUser().isAccountPrivate,

            errorVisible: false,
            errorMessage: null
        }
    },

    methods: {
        validationSuccessful: function () {
            if (this.name === null || this.name.trim() === '') {
                this.errorMessage = "Name field can't be empty";
                return false;
            }
            if (this.surname === null || this.surname.trim() === '') {
                this.errorMessage = "Surname field can't be empty";
                return false;
            }
            return true;
        },
        saveChanges: function (event) {
            if (!this.validationSuccessful()) {
                this.errorVisible = true;
                return;
            }
            this.errorVisible = false;

            let currentUser = window.getCurrentUser();
            currentUser.name = this.name;
            currentUser.surname = this.surname;
            currentUser.dateOfBirth = this.dateOfBirth;
            currentUser.gender = this.gender;
            currentUser.isAccountPrivate = this.isAccountPrivate;

            window.API.put("users", currentUser).then(res => {
                alert("Successfully changed info");
                this.errorVisible = false;
                localStorage.setItem("user", JSON.stringify(currentUser));
                if (window.getCurrentUser().role === 'ADMIN')
                    router.push('homePageAdmin');
                else
                    router.push('homePageRegular');
            }).catch(err => {
                alert("Error: " + err.response.data);
            })
        },
        reset: function (event) {
            this.username = window.getCurrentUser().username;
            this.name = window.getCurrentUser().name;
            this.surname = window.getCurrentUser().surname;
            this.dateOfBirth = window.getCurrentUser().dateOfBirth;
            this.gender = window.getCurrentUser().gender;
            this.isAccountPrivate = window.getCurrentUser().isAccountPrivate;
        }
    },


    template: `
    
<div class="reg-container">
  <div class="row">
        <div class="col-md-6 reg-form">
            <h5>Edit information</h5>
            <form>
                <div class="form-group">
                    <label for="username">Username</label>
                    <input id="username" type="text" class="form-control" disabled v-model="username"/>
                </div>
                <div class="form-group">
                    <label for="name">Name</label>
                    <input v-model="name" id="name" type="text" class="form-control"/>
                </div>
                <div class="form-group">
                    <label for="surname">Surname</label>
                    <input v-model="surname" id="surname" type="text" class="form-control"/>
                </div>
                <div class="form-group">
                    <label for="dateOfBirth">Date of birth</label>
                    <input v-model="dateOfBirth" id="dateOfBirth" type="date" class="form-control"/>
                </div>
                <div class="form-group">
                    <label><p>Gender:</p></label><br/>
                    
                    <input v-if="gender==='MALE'" selected v-model="gender" type="radio" id="Male" value=MALE>
                    <input v-else v-model="gender" type="radio" id="Male" value=MALE>
                    <label for="Male">Male</label>
                    
                    <input v-if="gender==='FEMALE'" selected v-model="gender" type="radio" id="Female" value=FEMALE>
                    <input v-else v-model="gender" type="radio" id="Female" value=FEMALE>
                    <label for="Female">Female</label>             
                </div>
                <div>
                    <input v-model="isAccountPrivate" type="checkbox" id="privateAccount">
                    <label for="privateAccount"> Private account</label><br>
                </div>
                <div>
                    <hr>
                    <div v-if="errorVisible" class="errorDiv">
                        <label class="errorMessage">{{ errorMessage }}</label>
                        <hr>
                    </div>
                                            
                    <input v-on:click="saveChanges($event)" type="button" class="btn btn-primary" value="Save" />
                    <input v-on:click="reset($event)" type="button" class="btn btn-primary" value="Reset" />
                </div>
            </form>
        </div>
    </div>
</div>
    `

});