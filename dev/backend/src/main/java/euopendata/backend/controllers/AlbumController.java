package euopendata.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import euopendata.backend.services.AlbumService;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping(path = "/album")
@AllArgsConstructor
public class AlbumController {
	private final AlbumService albumService;
	
	@Autowired
	public AlbumController (AlbumService albumService) {
		this.albumService = albumService;
	}
}
