
Vue.component("user-info", {
    props: ['user', 'view'],
    data: function () {
        return {
        }
    },

    computed: {
        numOfYears() {
            let ageDifMs = Date.now() - new Date(this.user.dateOfBirth);
            let ageDate = new Date(ageDifMs);
            return Math.abs(ageDate.getUTCFullYear() - 1970);
        }
    },

    methods: {
        userProfilePage: function () {
            console.log("idi na stranicu");
            console.log(this.user.username);
            router.push({
                name: 'userProfilePage',
                params: {
                    username: this.user.username
                }
            });
            this.$router.go();
        }
    },


    template: `
<div>
    
    <div v-if="view==='big'" class="profileContainerBigView" >
        <img class="profileImageIconBig" v-bind:src="user.profileImageUrl"  />
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        <table>
            <tr><td class="userInfoUsernameTextBigView">
                <a href="#" v-on:click="userProfilePage()" >@{{user.username}}</a>
            </td></tr>
            <tr>
                <td class="userInfoTextBigView">{{user.name}} {{user.surname}}</td>
                <td class="userInfoTextBigView" v-if="user.gender==='FEMALE'"><span class="fa fa-female"></span></td>
                <td class="userInfoTextBigView" v-else><span class="fa fa-male"></span></td>
            </tr>
            <tr>
                <td class="userInfoTextBigView">
                    {{numOfYears}} y.o. ({{user.dateOfBirth}})
                </td>
            </tr>
        </table>
    </div>
    <div v-else-if="view==='small'" class="profileContainerSmallView" on:click="userProfilePage" >
        <img class="profileImageIconMedium" v-bind:src="user.profileImageUrl" />
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        <table>
             <tr><td class="userInfoUsernameTextSmallView">
                <a href="#" v-on:click="userProfilePage()" >@{{user.username}}</a>
            </td></tr>
            <tr>
                <td class="userInfoTextSmallView">{{user.name}} {{user.surname}}</td>
                <td class="userInfoTextSmallView" v-if="user.gender==='FEMALE'"><span class="fa fa-female"></span></td>
                <td class="userInfoTextSmallView" v-else><span class="fa fa-male"></span></td>
            </tr>
            <tr>
                <td class="userInfoTextSmallView">
                    {{numOfYears}} y.o. ({{user.dateOfBirth}})
                </td>
            </tr>
        </table>
    </div>
</div>
    `


});