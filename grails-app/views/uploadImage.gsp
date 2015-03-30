
	<fieldset>
	  <legend>Upload Image</legend>
	  <g:uploadForm url="/museum/SetExhibitImage">
	    <input type="text" name="exhibitId" id="exhibitId" />
	    <label for="image">Image (16K)</label>
	    <input type="file" name="image" id="image" />    
	    <div style="font-size:0.8em; margin: 1.0em;">
	      For best results, your avatar should have a width-to-height ratio of 4:5.
	      For example, if your image is 80 pixels wide, it should be 100 pixels high.
	    </div>
	    <div>${flash.message}</div>
	    <input type="submit" class="buttons" value="Upload" />
	  </g:uploadForm>
	</fieldset>