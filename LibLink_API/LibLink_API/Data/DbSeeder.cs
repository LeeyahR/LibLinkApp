using LibLink_API.Models;

namespace LibLink_API.Data
{
    public static class DbSeeder
    {
        public static void Seed(LibLink_APIContext context)
        {
            if (context.Book.Any())
                return;

            context.Book.AddRange(

                new Book
                {
                    Title = "Clean Code",
                    Author = "Robert C. Martin",
                    ISBN = "9780132350884",
                    Genre = "Programming",
                    Description = "A guide to writing cleaner and more maintainable software.",
                    TotalCopies = 5,
                    AvailableCopies = 5
                },

                new Book
                {
                    Title = "The Pragmatic Programmer",
                    Author = "David Thomas",
                    ISBN = "9780135957059",
                    Genre = "Programming",
                    Description = "Practical approaches to software development.",
                    TotalCopies = 4,
                    AvailableCopies = 4
                },

                new Book
                {
                    Title = "Database System Concepts",
                    Author = "Abraham Silberschatz",
                    ISBN = "9780078022159",
                    Genre = "Database",
                    Description = "An introduction to database systems.",
                    TotalCopies = 3,
                    AvailableCopies = 3
                },

                new Book
                {
                    Title = "Artificial Intelligence",
                    Author = "Stuart Russell",
                    ISBN = "9780134610993",
                    Genre = "Artificial Intelligence",
                    Description = "Introduction to artificial intelligence concepts.",
                    TotalCopies = 3,
                    AvailableCopies = 3
                }
            );

            context.SaveChanges();
        }
    }
}
