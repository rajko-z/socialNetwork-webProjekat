Vue.component("message-component", {
    props: ['message'],
    data: function () {
        return {
            user: null,
            loggedUser: window.getCurrentUser()
        }
    },
    mounted: function() {

        console.log(this.logUsername);

    },

    template: `

     <div v-if ="message.from.username === loggedUser.username"class="d-flex justify-content-between align-items-left">
      
        <div class="ml-2">
            <div class="h6 m-0">
                @{{message.from.username}}
            </div>
            
             <div class="ml-2">
                <textarea class="commentInputField" disabled v-bind:value="message.text"/>
            </div>
             <span class="text-muted h8 mb-2" style="font-size: 9px"> <i class="fa fa-clock-o"></i>{{message.createdAt}}</span>
            
            <br/><br/>
        </div>
        <div class="mr-2">
            <img class="profileImageIconSmall" v-bind:src="message.from.profileImageUrl" alt="">
        </div>
     
     </div>    
    <div  v-else class="d-flex justify-content-between align-items-center">
        <div class="mr-2">
            <img class="profileImageIconSmall" v-bind:src="message.from.profileImageUrl" alt="">
        </div>
        <div class="ml-2">
            <div class="h6 m-0">
                @{{message.from.username}}
            </div>
            
             <div class="ml-2">
                <textarea class="commentInputField" disabled v-bind:value="message.text"/>
            </div>
             <span class="text-muted h8 mb-2" style="font-size: 9px"> <i class="fa fa-clock-o"></i>{{message.createdAt}}</span>
            
            <br/><br/>
        </div>
     
        
        
     
    </div>
    `


});