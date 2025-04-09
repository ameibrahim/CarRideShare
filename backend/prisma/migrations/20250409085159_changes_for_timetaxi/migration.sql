/*
  Warnings:

  - Made the column `profileImage` on table `User` required. This step will fail if there are existing NULL values in that column.

*/
-- AlterTable
ALTER TABLE `Ride` MODIFY `taxiArrivalTime` VARCHAR(191) NULL;

-- AlterTable
ALTER TABLE `User` MODIFY `profileImage` VARCHAR(191) NOT NULL DEFAULT 'default.jpg';
