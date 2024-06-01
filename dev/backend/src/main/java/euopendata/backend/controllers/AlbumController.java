package euopendata.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import euopendata.backend.models.Photo;
import euopendata.backend.services.AlbumService;

import java.util.List;


@RestController
@RequestMapping(path = "/album")
public class AlbumController {
	private final AlbumService albumService;
	
	@Autowired
	public AlbumController (AlbumService albumService) {
		this.albumService = albumService;
	}
	
	@PostMapping("/photo") 
	public void addPhoto(@RequestBody Photo photo) {
		albumService.addPhoto(photo);
	}

	@GetMapping("{token}/all-photos")
	public List<Photo> getAllPhotos(@PathVariable String token) {
		return albumService.getAllPhotosByToken(token);
	}
}
