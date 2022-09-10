
Vue.component("user-info", {
    props: ['user', 'view'],
    data: function () {
        return {
            loggedUser: window.getCurrentUser(),
            friendStatus: null,
            isBlocked: null,
        }
    },

    computed: {
        numOfYears() {
            let ageDifMs = Date.now() - new Date(this.user.dateOfBirth);
            let ageDate = new Date(ageDifMs);
            return Math.abs(ageDate.getUTCFullYear() - 1970);
        }
    },

    mounted: function () {
        this.isBlocked = this.user.isBlocked;
          if (this.loggedUser != null && this.loggedUser.role === 'REGULAR') {
              window.API.get("friendStatus/" + this.user.username).then(res => {
                  this.friendStatus = res.data;
              }).catch(err => {
                  alert(err.response.data);
              });
          }
    },

    methods: {
        openChat: function () {
            console.log("idi na chat sa");
            console.log(this.user.username);
            router.push({
                name: 'messages',
                params: {
                    username: this.user.username
                }
            });

            this.$router.go();
        },
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
        },

        sendFriendRequest: function() {
            window.API.post("friendStatus/sendRequestTo/" + this.user.username).then(res => {
                alert("You successfully sent friend request to" + this.user.username);
                this.friendStatus = 'PENDING';
            }).catch(err => {
                alert(err.response.data);
            });
        },
        changeStatus: function() {
            window.API.post("changeStatus/"+ this.user.username).then(res => {
                alert("You are changed staus of " + this.user.username);
                console.log(this.isBlocked);
                if(this.isBlocked){
                    this.isBlocked = false;
                }
                else
                {this.isBlocked= true;}
                console.log(this.isBlocked);
            }).catch(err => {
                alert(err.response.data);
            });
        },

        acceptFriendRequest: function() {
            window.API.put("friendStatus/acceptRequest/" + this.user.username).then(res => {
                alert("You are now friend with " + this.user.username);
                this.friendStatus = 'FRIENDS';
            }).catch(err => {
                alert(err.response.data);
            });
        },

        unfollow: function () {
            window.API.delete("friendStatus/removeFriend/" + this.user.username).then(res => {
                alert("You successfully unfollowed " + this.user.username);
                this.friendStatus = 'NOT_FRIENDS';
            }).catch(err => {
                alert(err.response.data);
            });
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
        
        <div v-if="loggedUser != null && loggedUser.role==='REGULAR' && loggedUser.username != user.username" style="margin-left: 40%; margin-top: 15%">
            <button v-on:click="openChat()" class="btn btn-primary"><i class="fa fa-paper-plane"></i>&nbsp;Send message</button>
            &nbsp;&nbsp;
            <button v-on:click="sendFriendRequest()" v-if="friendStatus==='NOT_FRIENDS'" class="btn btn-primary"><i class="fa fa-plus"></i>&nbsp;Send friend request</button>
            <button v-on:click="unfollow()" v-else-if="friendStatus==='FRIENDS'" class="btn btn-primary"><i class="fa fa-minus"></i>&nbsp;Unfollow</button>
            <button v-on:click="acceptFriendRequest()" v-else-if="friendStatus==='ACCEPT'" class="btn btn-primary"><i class="fa fa-check"></i>&nbsp;Accept request</button>
            <button v-else class="btn btn-primary"><i class="fa fa-hourglass-start"></i>&nbsp;Pending...</button>
        </div>
        
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
       <div v-if="loggedUser != null && loggedUser.role==='ADMIN'" style="margin-left: 15%">
       
             <button v-on:click="openChat()" class="btn btn-primary"><i class="fa fa-paper-plane"></i>&nbsp;Send message</button>
            &nbsp;&nbsp;
            <button title="Unblock" style="font-size: 14px;color:darkblue" v-on:click="changeStatus()" v-if="isBlocked" class="btn"><i class="fa fa-plus"></i></button>
             <button title="Block" style="font-size: 14px;color:darkblue" v-on:click="changeStatus()" v-else-if="!isBlocked" class="btn"><i class="fa fa-minus"></i></button>
           
           
        </div>
        
         <div v-if="loggedUser != null && loggedUser.role==='REGULAR' && loggedUser.username != user.username" style="margin-left: 15%">
            <button title="Send message" style="font-size: 14px;color:darkblue" v-on:click="openChat()" class="btn"><i class="fa fa-paper-plane"></i></button>
            <button title="Send friend request" style="font-size: 14px;color:darkblue" v-on:click="sendFriendRequest()" v-if="friendStatus==='NOT_FRIENDS'" class="btn"><i class="fa fa-plus"></i></button>
            <button title="Unfollow" style="font-size: 14px;color:darkblue" v-on:click="unfollow()" v-else-if="friendStatus==='FRIENDS'" class="btn"><i class="fa fa-minus"></i></button>
            <button title="Accept friend request" style="font-size: 14px;color:darkblue" v-on:click="acceptFriendRequest()" v-else-if="friendStatus==='ACCEPT'" class="btn"><i class="fa fa-check"></i></button>
            <button title="Waiting on user" style="font-size: 14px;color:darkblue" v-else class="btn"><i class="fa fa-hourglass-start"></i></button>
        </div>
    </div>
</div>
    `


});