using System.Text.RegularExpressions;

namespace Frikollection_Api.Helpers
{
    public class ImageHelper
    {
        private static readonly string UploadsFolder = Path.Combine("wwwroot", "uploads", "tcg");

        public static async Task<string?> DownloadAndSaveImageAsync(string imageUrl, string imageName)
        {
            try
            {
                if (string.IsNullOrWhiteSpace(imageUrl) || string.IsNullOrWhiteSpace(imageName))
                    return null;

                var extension = Path.GetExtension(imageUrl);
                if (extension != ".png")
                    return null;

                var sanitizedFileName = SanitizeFileName(imageName + extension);
                var savePath = Path.Combine(UploadsFolder, sanitizedFileName);

                if (!Directory.Exists(UploadsFolder))
                    Directory.CreateDirectory(UploadsFolder);

                using var client = new HttpClient();
                var imageData = await client.GetByteArrayAsync(imageUrl);
                await File.WriteAllBytesAsync(savePath, imageData);

                // Retorna el path relatiu que es combina amb el host
                return $"/images/uploads/tcg/{sanitizedFileName}";
            }
            catch
            {
                return null;
            }
        }

        private static string SanitizeFileName(string name)
        {
            var extension = Path.GetExtension(name);
            var baseName = Path.GetFileNameWithoutExtension(name);

            // Substitueix caràcters no vàlids per "_"
            var sanitized = Regex.Replace(baseName.ToLowerInvariant(), @"[^a-z0-9]", "_");

            // Redueix múltiples guions baixos seguits a un de sol
            sanitized = Regex.Replace(sanitized, @"_+", "_");

            return $"{sanitized}{extension}";
        }
    }
}
