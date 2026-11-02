using Microsoft.EntityFrameworkCore.Migrations;

#nullable disable

namespace LibLink_API.Migrations
{
    /// <inheritdoc />
    public partial class AddGoogleSubject : Migration
    {
        /// <inheritdoc />
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.AddColumn<string>(
                name: "GoogleSubject",
                table: "User",
                type: "nvarchar(max)",
                nullable: true);
        }

        /// <inheritdoc />
        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropColumn(
                name: "GoogleSubject",
                table: "User");
        }
    }
}
