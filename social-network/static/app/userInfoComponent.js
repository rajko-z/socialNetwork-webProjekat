
Vue.component("user-info", {
    props: ['user'],
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

    template: `
<div>
    <div class="profileContainer">
        <img class="profileImageIcon" v-bind:src="user.profileImageUrl" />
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        <table>
            <tr><td class="userInfoUsernameText">@{{user.username}}</td></tr>
            <tr>
                <td class="userInfoText">{{user.name}} {{user.surname}}</td>
                <td class="userInfoText" v-if="user.gender==='FEMALE'"><span class="fa fa-female"></span></td>
                <td class="userInfoText" v-else><span class="fa fa-male"></span></td>
            </tr>
            <tr>
                <td class="userInfoText">
                    {{numOfYears}} y.o. ({{user.dateOfBirth}})
                </td>
            </tr>
        </table>
    </div>
</div>
    `


});