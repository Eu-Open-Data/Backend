package euopendata.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import euopendata.backend.models.Photo;
import euopendata.backend.services.AlbumService;


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
}
