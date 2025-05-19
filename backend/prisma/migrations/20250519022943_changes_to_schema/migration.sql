/*
  Warnings:

  - You are about to drop the `FAQ` table. If the table is not empty, all the data it contains will be lost.

*/
-- AlterTable
ALTER TABLE `Ride` ADD COLUMN `name` VARCHAR(191) NOT NULL DEFAULT '';

-- DropTable
DROP TABLE `FAQ`;
