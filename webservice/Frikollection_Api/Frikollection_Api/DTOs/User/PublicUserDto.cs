namespace Frikollection_Api.DTOs.User
{
    public class PublicUserDto
    {
        public string? Avatar { get; set; }
        public string? Nickname { get; set; }
        public string? FirstName { get; set; }
        public string? LastName { get; set; }
        public DateOnly? Birthday { get; set; }
        public string? Biography { get; set; }
    }
}
