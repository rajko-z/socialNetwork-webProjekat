
Vue.component("change-password", {
    data: function () {
        return {
            loggedUser: window.getCurrentUser(),
            currentPassword: null,
            newPassword: null,
            controlPassword: null,

            errorVisible: false,
            errorMessage: null
        }
    },

    mounted: function () {
        if (window.getCurrentUser() === null) {
            router.push('/');
        }
    },

    methods: {
        validationSuccessful: function() {
            if (this.currentPassword == null || this.currentPassword.trim() === '') {
                this.errorMessage = "Please type current password";
                return false;
            }
            if (this.newPassword == null || this.newPassword.trim() === '') {
                this.errorMessage = "Please type new password";
                return false;
            }
            if (this.controlPassword == null || this.controlPassword.trim() === '') {
                this.errorMessage = "Please retype new password";
                return false;
            }
            if (this.controlPassword !== this.newPassword) {
                this.errorMessage = "Control password is not same as new one";
                return false;
            }
            return true;
        },

        changePassword: function(event) {
            event.preventDefault();
            if (!this.validationSuccessful()) {
                this.errorVisible = true;
                return;
            }
            const updatePasswordRequest = {
                "username": this.loggedUser.username,
                "currentPassword": this.currentPassword,
                "newPassword": this.newPassword
            }

            window.API.put("users/changePassword", updatePasswordRequest).then(res => {
                alert(res.data);
                this.errorVisible = false;
                if (window.getCurrentUser().role === 'ADMIN')
                    router.push('homePageAdmin');
                else
                    router.push('homePageRegular');
            }).catch(err => {
                alert(err.response.data);
            })
        }
    },

    template: `
<div class="login-container">
    <div class="row">
        <div class="col-md-6 login-form">
            <h3>Change Password</h3>
            <form>
                <div class="form-group">
                    <input v-model="currentPassword" type="password" class="form-control" placeholder="Your Current Password *" value="" />
                </div>
                <div class="form-group">
                    <input v-model="newPassword" type="password" class="form-control" placeholder="Your New Password *" value="" />
                </div>
                <div class="form-group">
                    <input v-model="controlPassword" type="password" class="form-control" placeholder="Retype New Password *" value="" />
                </div>
                <hr/>
                <div class="form-group" v-if="errorVisible">
                    <label class="errorMessage">{{ errorMessage }}</label>
                </div>
                <div class="form-group">
                    <input v-on:click="changePassword($event)" type="submit" value="Save" class="login-btnSubmit"  />
                </div>
            </form>
        </div>
    </div>
</div> 
    `


});