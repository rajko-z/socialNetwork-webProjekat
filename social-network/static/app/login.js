
Vue.component("login", {
    data: function () {
        return {
            errorVisible: false,
            username: null,
            password: null
        }
    },

    methods: {
        sendLoginEvent : function() {
            this.$root.$emit('userLoggedIn');
        },

        continueAsGuest : function() {
            router.push('homePageGuest');
        },

        goToRegistrationPage: function() {
            router.push('registration');
        },

       login: function(event) {
           event.preventDefault();
           const postRequest = {
               "username": this.username,
               "password": this.password
           };
           window.API.post("auth", postRequest).then(res => {
                let user = {
                    "username": res.data.username,
                    "name": res.data.name,
                    "surname": res.data.surname,
                    "dateOfBirth": res.data.dateOfBirth,
                    "gender": res.data.gender,
                    "role": res.data.role,
                    "isAccountPrivate": res.data.isAccountPrivate
                };
                let jwt = res.data.jwt;
                localStorage.setItem("jwt", jwt);
                localStorage.setItem("user", JSON.stringify(user));

                this.sendLoginEvent();

                if (user.role === 'REGULAR') {
                    router.push('homePageRegular');
                } else if (user.role === 'ADMIN') {
                    router.push('homePageAdmin');
                }

           }).catch(err => {
                this.errorVisible = true;
           })
       }
    },

    template: `
<div class="login-container">
    <div class="row">
        <div class="col-md-6 login-form">
            <h3>Login</h3>
            <form>
                <div class="form-group">
                    <input v-model="username" type="text" class="form-control" placeholder="Your Username *" value="" />
                </div>
                <div class="form-group">
                    <input v-model="password" type="password" class="form-control" placeholder="Your Password *" value="" />
                </div>
                <div class="form-group">
                    <input v-on:click="login($event)" type="submit" class="login-btnSubmit" value="Login" />
                </div>
                <div class="form-group" v-if="errorVisible">
                    <label class="errorMessage">Bad credentials.</label>
                </div>
                
                <div class="form-group">
                    <a href="#" v-on:click="goToRegistrationPage" class="text">Don't have an account? Register here</a>
                </div>
                <hr/>
                <div class="form-group">
                    <a href="#" v-on:click="continueAsGuest" class="text">Continue as a guest</a>
                </div>
            </form>
        </div>
    </div>
</div> 
    `


});