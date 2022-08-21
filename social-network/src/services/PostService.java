package services;

import dto.NewPostDTO;
import exceptions.BadRequestException;
import exceptions.InternalAppException;
import model.Image;
import model.Post;
import model.User;
import repository.PostRepository;
import repository.RepoFactory;
import util.Constants;
import validation.ValidationService;

import java.time.LocalDateTime;

public class PostService {
    private final PostRepository postRepository;
    private final ImageService imageService;
    private final ValidationService validationService;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
        this.validationService = new ValidationService();
        this.imageService = new ImageService(RepoFactory.imageRepo);
    }

    public Post addPost(NewPostDTO newPostDTO, User user) {
        String errorMsg = validationService.validateAndGetErrorMessage(newPostDTO);
        if (errorMsg != null) {
            throw new BadRequestException(errorMsg);
        }
        switch (newPostDTO.getType()) {
            case REGULAR_POST: {
                return createRegularPost(newPostDTO, user);
            }
            case IMAGE_POST: {
                return createImagePost(newPostDTO, user);
            }
            case PROFILE_POST: {
                return createProfileImagePost(newPostDTO, user);
            }
            default:
                throw new BadRequestException("Unknown post type: " + newPostDTO.getType().toString());
        }
    }

    private Post createRegularPost(NewPostDTO newPostDTO, User user) {
        if (newPostDTO.getText() == null || newPostDTO.getText().trim().isEmpty())
            throw new BadRequestException("Regular post can't have empty content");

        Image postImage = null;
        if (newPostDTO.getBase64Image() != null) {
            if (newPostDTO.getImageName() == null || newPostDTO.getImageName().trim().isEmpty()){
                throw new BadRequestException("Image name should be specified");
            }
            postImage = imageService.createImageFromBase64String(newPostDTO.getBase64Image(), newPostDTO.getImageName());
            if (postImage == null) {
                throw new InternalAppException("Error happened while adding image for post");
            }
        }

        Post post = new Post(newPostDTO.getText(), newPostDTO.getType(), LocalDateTime.now(), false);
        post.setImage(postImage);
        Post addedPost = this.postRepository.add(post);
        user.addNewPost(post);
        saveDataAfterAddingPost();
        return addedPost;
    }

    private void saveDataAfterAddingPost() {
        this.postRepository.saveData(Constants.FILE_POSTS_HEADER);
        RepoFactory.userRepo.saveData(Constants.FILE_USERS_HEADER);
        RepoFactory.imageRepo.saveData(Constants.FILE_IMAGES_HEADER);
    }

    private Post createImagePost(NewPostDTO newPostDTO, User user) {
        return null;
    }

    private Post createProfileImagePost(NewPostDTO newPostDTO, User user) {
        return null;
    }
}
