// src/services/upload.service.js
import api from "./base.service";

class UploadService {
  uploadFile(formData) {
    return api.post("/upload", formData); 
  }
}

const uploadService = new UploadService();
export default uploadService;
