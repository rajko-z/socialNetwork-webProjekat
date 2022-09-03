Vue.component("edit-profile-image", {
    data: function() {
        return {
            loggedUser: window.getCurrentUser(),
            imageUrl: window.getCurrentUser().imageUrl,
            fileChosen: false,
            imageName: null
        }
    },

    methods: {

        getBase64: function (file) {
            return new Promise((resolve, reject) => {
                const reader = new FileReader();
                reader.readAsDataURL(file);
                reader.onload = () => resolve(reader.result);
                reader.onerror = error => reject(error);
            });
        },

        previewImage: function (event) {
            let files = event.target.files;
            console.log(files);
            if (files.length > 0) {
                let file = files[0];
                console.log("ispisujem fajl");
                console.log(file);


                if (file['type'].split('/')[0] !== 'image') {
                    alert("Please choose image");
                    this.reset();
                    return;
                }

                this.getBase64(file).then(
                    data => {
                        this.imageUrl = data;
                        this.fileChosen = true;
                        this.imageName = file.name;
                        console.log(data);
                    }
                );
            }
        },

        reset: function () {
            this.imageUrl = window.getCurrentUser().imageUrl;
            this.$refs.profileFile.value = null;
            this.fileChosen = false;
        },

        saveProfileImage: function () {
            let base64Part = this.imageUrl.split(',')[1];
            let imagePostRequest = {
                "type": "PROFILE_POST",
                "imageName": this.imageName,
                "base64Image": base64Part
            };

            window.API.post("posts", imagePostRequest).then(res => {
               let createdPost = res.data;
               let newImageName = createdPost.image.name;

               let loggedUser = window.getCurrentUser();
               let currentUrl = loggedUser.imageUrl;
               let tokens = currentUrl.split("/images/");
               loggedUser.imageUrl = tokens[0] + "/images/" + newImageName;
               localStorage.setItem("user", JSON.stringify(loggedUser));

               alert("Successfully changed profile image");

            }).catch(err => {
                alert("Error:" + err.response.data);
            })
        }
    },


    template: `

<div class="reg-container">
  <div class="row">
        <div class="col-md-6 reg-form">
            <h5>Change profile image</h5>
            <img v-bind:src="imageUrl" class="profileImageIcon">
            <br/>
            <hr>
            <div>
                <input type="file" ref="profileFile" @change="previewImage" class="registration-btnSubmit" value="Upload" />
                <br/>
                <hr/>
                <input v-on:click="reset($event)" type="button" class="registration-btnSubmit" value="Reset" />
                
                <input v-if="fileChosen" v-on:click="saveProfileImage($event)" type="button" class="registration-btnSubmit" value="Save" />                
            </div>
        </div>
    </div>
</div>
    `

});